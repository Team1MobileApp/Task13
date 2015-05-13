/******************************
 * Author: Biqiu Li
 * Date: 2015/05/09
 ******************************/
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import databeans.CandidateRoute;
import databeans.RouteLocation;
import databeans.RouteStep;
import databeans.StepType;
import sun.net.www.protocol.http.HttpURLConnection;
import model.Model;
import model.StopDAO;

class BusStop {
    public String Name;
    public int Id;
    public double Latitute, Longitute;
}

class SegmentEstimation {
    public String RouteName;
    public Calendar ArrivalTime, DepartureTime;
}

class RouteEstimation {
    public Calendar ArrivalTime;
    public List<SegmentEstimation> Segments;
}

class BusPrediction {
    int VehicleId;
    Calendar Time;
}

public class RouteAction extends Action {

    private StopDAO stopDAO;

    public RouteAction(Model model) {
        stopDAO = model.getStopDAO();
    }

    @Override
    public String getName() {
        return "route_plan.do";
    }

    public static final String formatDate(Date dt, String format) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dt);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(cal.getTime()));
    }

    @Override
    public String perform(HttpServletRequest request) {
        String origin = request.getParameter("origin");
        String dest = request.getParameter("destination");
        if (origin == null)
            origin = "5000 forbes ave, pittsburgh, pa";
        if (dest == null)
            dest = "10 second ave, pittsburgh, pa";

        try {
            List<CandidateRoute> routes = getAllRoutes(origin, dest);
            List<RouteEstimation> estimations = estimateRoutes(routes);
            for (int i = 0; i<routes.size(); i++)
            {
                String time = "N/A";
                if (i < estimations.size())
                {
                    time = formatDate(estimations.get(i).ArrivalTime.getTime(),
                            "MM/dd HH:mm");
                }
                routes.get(i).setEstimatedArrivalTime(time);
            }
            request.getSession().setAttribute("Routes", routes);
            request.getSession().setAttribute("Estimations", estimations);
            for (CandidateRoute r : routes) {
                System.out.println("Route:");
                for (RouteStep s : r.Steps) {
                    System.out.println("  step " + s.Duration + "s, start="
                            + s.StartPos.Name + " end = " + s.EndPos.Name
                            + "; route = " + s.BusRoute);
                }
                System.out.println("----");
            }
            for (RouteEstimation estimate : estimations) {
                System.out.println(formatDate(estimate.ArrivalTime.getTime(),
                        "MM/dd HH:mm"));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RollbackException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        request.getSession().setAttribute("fuck", "testvalue");
        return "ViewRoute.jsp";
    }

    private static List<RouteEstimation> estimateRoutes(
            List<CandidateRoute> routes) throws IOException, RollbackException {
        List<RouteEstimation> result = new ArrayList<RouteEstimation>();
        for (CandidateRoute route : routes) {
            RouteEstimation restimate = new RouteEstimation();
            restimate.Segments = new ArrayList<SegmentEstimation>();
            Calendar arrivalTime = Calendar.getInstance();
            for (RouteStep step : route.Steps) {
                if (step.Type.equals(StepType.Walk)) {
                    arrivalTime.add(Calendar.SECOND, step.Duration);
                } else if (step.Type.equals(StepType.Bus)) {
                    List<String> directions = getRouteDirections(step.BusRoute);
                    String direction = directions.get(0);
                    for (String dir : directions)
                        if (step.FullRouteName.toUpperCase().contains(
                                dir.toUpperCase())) {
                            direction = dir;
                            break;
                        }
                    List<BusStop> stops = getStops(step.BusRoute, direction);
                    int startStopId = -1, endStopId = -1;
                    if (stops != null) {
                        for (BusStop s : stops) {
                            if (s.Name.toUpperCase().equals(
                                    step.StartPos.Name.toUpperCase()))
                                startStopId = s.Id;
                            if (s.Name.toUpperCase().equals(
                                    step.EndPos.Name.toUpperCase()))
                                endStopId = s.Id;
                        }
                    }
                    BusPrediction earliestBus = null;

                    List<BusPrediction> predictions = getBusPrediction(
                            step.BusRoute, startStopId);

                    if (predictions != null) {
                        for (BusPrediction b : predictions) {
                            if (b.Time.compareTo(arrivalTime) > 0) {
                                earliestBus = b;
                                break;
                            }
                        }
                    }
                    if (earliestBus != null && earliestBus.VehicleId != 0) {
                        SegmentEstimation sest = new SegmentEstimation();
                        sest.RouteName = step.BusRoute;

                        Calendar parrivalTime = predictBusArrivalTime(
                                step.BusRoute, earliestBus.VehicleId, endStopId);
                        if (parrivalTime != null) {
                            arrivalTime = parrivalTime;
                            sest.ArrivalTime = parrivalTime;
                            sest.DepartureTime = earliestBus.Time;
                            restimate.Segments.add(sest);
                        } else
                            arrivalTime.add(Calendar.SECOND, step.Duration);
                    } else
                        arrivalTime.add(Calendar.SECOND, step.Duration);
                }
            }
            restimate.ArrivalTime = arrivalTime;
            result.add(restimate);
        }
        return result;
    }

    private static Calendar predictBusArrivalTime(String routeName, int vid,
            int stopId) {
        try {
            HttpURLConnection connection = null;
            URL url = new URL(
                    "http://truetime.portauthority.org/bustime/api/v2/getpredictions?key=ADpCvpyDcupACyuMdk5wrVTVH&format=json&rt="
                            + URLEncoder.encode(routeName, "UTF-8")
                            + "&vid="
                            + vid);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            JSONObject obj = (JSONObject) JSONValue
                    .parse(readResponse(connection));
            JSONArray predicts = (JSONArray) ((JSONObject) obj
                    .get("bustime-response")).get("prd");
            for (Object predict : predicts) {
                int pstpid = Integer.parseInt(((JSONObject) predict).get(
                        "stpid").toString());
                if (pstpid == stopId) {
                    Calendar time = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyyMMdd HH:mm", Locale.US);
                    time.setTime(sdf.parse(((JSONObject) predict).get("prdtm")
                            .toString()));
                    return time;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static List<BusPrediction> getBusPrediction(String routeName,
            int stopId) {
        HttpURLConnection connection = null;
        URL url;
        try {
            url = new URL(
                    "http://truetime.portauthority.org/bustime/api/v2/getpredictions?key=ADpCvpyDcupACyuMdk5wrVTVH&format=json&rt="
                            + URLEncoder.encode(routeName, "UTF-8")
                            + "&stpid="
                            + stopId);
        } catch (MalformedURLException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            JSONObject obj = (JSONObject) JSONValue
                    .parse(readResponse(connection));
            JSONArray predicts = (JSONArray) ((JSONObject) obj
                    .get("bustime-response")).get("prd");
            List<BusPrediction> result = new ArrayList<BusPrediction>();
            for (Object predict : predicts) {
                Calendar time = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm",
                        Locale.US);
                time.setTime(sdf.parse(((JSONObject) predict).get("prdtm")
                        .toString()));
                int vid = Integer.parseInt(((JSONObject) predict).get("vid")
                        .toString());
                BusPrediction p = new BusPrediction();
                p.Time = time;
                p.VehicleId = vid;
                result.add(p);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private static List<String> getRouteDirections(String routeName)
            throws IOException, RollbackException {
        HttpURLConnection connection = null;
        URL url = new URL(
                "http://truetime.portauthority.org/bustime/api/v2/getdirections?key=ADpCvpyDcupACyuMdk5wrVTVH&format=json&rt="
                        + URLEncoder.encode(routeName, "UTF-8"));

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);

        JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
        JSONArray directions = (JSONArray) ((JSONObject) obj
                .get("bustime-response")).get("directions");
        List<String> result = new ArrayList<String>();
        for (Object dir : directions) {
            result.add(((JSONObject) dir).get("dir").toString());
        }
        return result;
    }

    private static List<BusStop> getStops(String routeName, String dirName)
            throws IOException, RollbackException {
        HttpURLConnection connection = null;
        URL url = new URL(
                "http://truetime.portauthority.org/bustime/api/v2/getstops?key=ADpCvpyDcupACyuMdk5wrVTVH&format=json&rt="
                        + URLEncoder.encode(routeName, "UTF-8")
                        + "&dir="
                        + URLEncoder.encode(dirName, "UTF-8"));

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);

        JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
        JSONArray stops = (JSONArray) ((JSONObject) obj.get("bustime-response"))
                .get("stops");
        List<BusStop> result = new ArrayList<BusStop>();
        for (Object stop : stops) {
            BusStop s = new BusStop();
            s.Id = Integer
                    .parseInt(((JSONObject) stop).get("stpid").toString());
            s.Name = ((JSONObject) stop).get("stpnm").toString();
            s.Latitute = Double.parseDouble(((JSONObject) stop).get("lat")
                    .toString());
            s.Longitute = Double.parseDouble(((JSONObject) stop).get("lon")
                    .toString());
            result.add(s);
        }
        return result;
    }

    private static List<CandidateRoute> getAllRoutes(String origin, String dest)
            throws IOException, RollbackException {
        HttpURLConnection connection = null;
        URL url = new URL(
                "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + URLEncoder.encode(origin, "UTF-8") + "&destination="
                        + URLEncoder.encode(dest, "UTF-8")
                        + "&sensor=false&mode=transit&alternatives=true");

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);

        JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
        JSONArray routes = (JSONArray) obj.get("routes");
        List<CandidateRoute> result = new ArrayList<CandidateRoute>();
        for (Object route : routes) {
            CandidateRoute parsedRoute = new CandidateRoute();
            result.add(parsedRoute);
            JSONObject jroute = (JSONObject) route;
            JSONArray legs = (JSONArray) jroute.get("legs");
            for (Object leg : legs) {
                JSONArray steps = (JSONArray) ((JSONObject) leg).get("steps");
                for (Object step : steps) {
                    RouteStep s = new RouteStep();

                    JSONObject jstep = (JSONObject) step;
                    s.Duration = Integer.parseInt(((JSONObject) jstep
                            .get("duration")).get("value").toString());
                    String travelMode = jstep.get("travel_mode").toString();
                    s.StartPos = new RouteLocation();
                    s.EndPos = new RouteLocation();
                    s.StartPos.Latitute = ((JSONObject) jstep
                            .get("start_location")).get("lat").toString();
                    s.StartPos.Latitute = ((JSONObject) jstep
                            .get("start_location")).get("lng").toString();
                    s.EndPos.Latitute = ((JSONObject) jstep.get("end_location"))
                            .get("lat").toString();
                    s.EndPos.Latitute = ((JSONObject) jstep.get("end_location"))
                            .get("lng").toString();
                    if (travelMode.equals("WALKING")) {
                        s.Type = StepType.Walk;
                        parsedRoute.Steps.add(s);
                    } else if (travelMode.equals("TRANSIT")) {
                        s.Type = StepType.Bus;
                        JSONObject transitDetails = (JSONObject) jstep
                                .get("transit_details");
                        s.BusRoute = ((JSONObject) transitDetails.get("line"))
                                .get("short_name").toString();
                        s.FullRouteName = ((JSONObject) transitDetails
                                .get("line")).get("name").toString();
                        s.StartPos.Name = ((JSONObject) transitDetails
                                .get("departure_stop")).get("name").toString();
                        s.EndPos.Name = ((JSONObject) transitDetails
                                .get("arrival_stop")).get("name").toString();
                        parsedRoute.Steps.add(s);
                    }
                }
            }
            parsedRoute.ComputeSummary();
        }
        return result;
    }

    private static String readResponse(HttpURLConnection connection) {
        try {
            StringBuilder str = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                str.append(line + System.getProperty("line.separator"));
            }
            return str.toString();
        } catch (IOException e) {
            return new String();
        }
    }

}