
  var datalists   = {};    // This object stores the lists of items for each combobox
  var visiblelist = null;  // This variable stores the id of the currently visible list
  var nochange    = false; // This variable signals the change event handler to abort
  var mousedown   = false; // This variable signals whether or not the left mouse button is down
  
   function init_comboboxes()
  {
    /* Store lists of items in each combo box for use later. */
    datalists['routeIds'] = load_datalist('routeIds');
    datalists['streets']  = load_datalist('streets');
    
    /* Add the list that will appear beneath the text fields. */
    $('body').append("<ul id='combo_list'></ul>");
  }
  
   function load_datalist(id)
  {
    var list = [], datalist;
  
    /* Get the list of <option val=""> elements in this datalist 
       then load the string of each option into the array. */
        
    datalist = $("#" + id + " option");
    for(var i = 0; i < datalist.size(); i++)
      list.push($(datalist[i]).val());
      
    return list;
  }

   function hide_list()
  {
    $('#combo_list').hide();
    visiblelist = null;
  }
  
   function show_list(control, value, listid)
  {
    var popup = $('#combo_list'), index, offset, width, height, valuelc;
    
    /* Make all letters lower case because we'll be doing case-insensitive comparison */
    valuelc = value.toLowerCase();
    
    popup.empty();
    for(var i = 0; i < datalists[listid].length; i++)
    {
      if(value == "") // If text field is empty just add all the strings to the pop-up list
        popup.append("<li>" + htmlspecialchars(datalists[listid][i]) + "</li>");
      else
      { 
        index = datalists[listid][i].toLowerCase().indexOf(valuelc);
        if(index != -1) // If value occurs in string, add it to the pop-up list
          popup.append("<li>" + htmlspecialchars(datalists[listid][i]) + "</li>");
      }
    }
    
    /* If list is empty don't show it. */
    if(popup.children('*').size() == 0)
    {
      hide_list();
      return; 
    }
    
    /* If list has only one item and it's equal to the text field value, don't show the list. */
    if(popup.children('*').size() == 1 && popup.children('*').html() == value)
    {
      hide_list();
      return; 
    }
    
    /* Get offset and height of text field and use it to set popup offset. */
    offset = $(control).offset();
    height = $(control).outerHeight();
    width  = $(control).outerWidth();
    
    popup.show(); 
    popup.width(width).offset({'left': offset['left'],'top': offset['top'] + height + 2});

    /* Set the id of the list that's currently visible. */
    visiblelist = listid;
  }
  
   function combolist_clicking()
  {
    return $("#combo_list").is(":focus") || ($('#combo_list').is(':hover') && mousedown);
  }
  
   function htmlspecialchars(unsafe)
  {
    /* Create HTML literal by escaping unsafe characters. */
    return unsafe
         .replace(/&/g, "&amp;")
         .replace(/</g, "&lt;")
         .replace(/>/g, "&gt;")
         .replace(/"/g, "&quot;")
         .replace(/'/g, "&#039;");
  }
 
  $(document).ready(init_comboboxes).on('mousedown','*',function()
  {
    mousedown = true;
  }).on('mouseup','*',function()
  {
    mousedown = false;
  })
  .on('click','#combo_list li',function()
  {
    var texfield = $("input[data-list=" + visiblelist + "]")[0];

    /* Set text field value to the string of the clicked 
       pop-up list item and update the pop-up list. */
    nochange = true;
    $(texfield).val($(this).html());
    show_list(texfield, $(texfield).val().trim(), visiblelist);
   
    hide_list();
    
  }).on('focusin','input[data-list]',function()
  {
    nochange = false;
    show_list(this, $(this).val().trim(), $(this).attr('data-list'));
    
  }).on('focusout','input[data-list]',function()
  {
    /* If keyboard focus leaves one of the text fields and the focus has
       not been transferred to the pop-up menu then hide the menu.*/

    if(!combolist_clicking())
      hide_list();
    
  }).on('input','input[data-list]',function()
  {
    show_list(this, $(this).val().trim(), $(this).attr('data-list'));
    
  }).on('change','input[data-list]',function()
  {
    var id, value, valid;

    /* If the nochange is true or the combolist is being clicked */
    if(nochange || combolist_clicking())
    {
      nochange = false;
      return;
    }
      
    /* Get the id of the list being shown and the entered value. */
    id    = $(this).attr('data-list');
    value = $(this).val();
    
    /* Check if the value is one of the values in the list. */
    valid = false;
    for(var i = 0; i < datalists[id].length; i++)
      if(datalists[id][i] == value)
      {
        valid = true;
        break;
      }
      
    /* If the value is not in the list then clear the input box. */
    if(!valid)
      $(this).val("");
  });
  