
  /* I use the following variable in order to ensure that the input 
     is only cleared the first time the user clicks in the text field  */
     
  var origin_first_focus = true; 
  
  /* I'm using the focusin event (instead of the click event) so that the 
     functionality is also activated when the user uses the tab key to navigate 
     to the text field. */
     
  $(document).on('focusin', '#origin', function()
  {
    if(origin_first_focus)
    {
      origin_first_focus = false;
      $(this).val("");
    }
  });