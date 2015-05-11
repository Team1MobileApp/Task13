package formbeans;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class UserForm extends FormBean {
	private String userId;

	public String getUserId() { return userId;    }
	
	public int getIdAsInt() {
		try {
			return Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			// call getValidationErrors() to detect this
			return -1;
		}
	}
	public void setUserId(String id) { this.userId = id; }

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		
		if (userId == null || userId.length() == 0) {
			errors.add("User ID is required");
			return errors;
		}

		try {
			Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			errors.add("User ID is not an integer");
		}
		
		return errors;
	}
}
