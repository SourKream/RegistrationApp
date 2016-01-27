# RegistrationApp

Basic single view Android Application for POSTing data to server using android Volley Library.

**Contributors**: [Karan Singh Chauhan](https://github.com/KSC2910), [Shantanu Kumar](https://github.com/SourKream), [Surag Nair](https://github.com/suragnair)

## Introduction

The main objective of the app is to provide a platform to the students of COP290@IITD to register their teams on the main server. Our app has a single view user friendly interface with added features to give users an easy experience for team registration. It sends data to the setup server via an HTTP POST request.

## User Interface

- The app has a single activity (MainActivity) which is invoked when the app is started. The layout has five (plus two hidden) text-fields for entering the team details i.e. the teamname along with the name and entry number of all members. The two hidden text-fields are controlled using a button to toggle the team size between 2 and 3. The text-boxes on the main screen have hints displayed on them indicating what the field is meant for. These disappears on adding text to the text-box. If the user leaves any field empty or enters invalid information then on submitting an error is displayed over the text field.

<p align="center">
<img src="/ReadMe Assets/Screenshot1.png" width="300">
<img src="/ReadMe Assets/Screenshot2.png" width="300">
</p>

- The Main Activity has four buttons namely 'Submit', 'Add Member', 'Remove Member' and 'Clear'. The basic functionality of each button is as follows :

  * SUBMIT : This button is used to submit the form and send the request to the server with the team details. An appropriate message is then displayed based on server's response. Before sending the request to the server an error check is also performed for validation of all the entered data.
  * ADD MEMBER '+' : This button provides an option to the users to add a team member. The default size of the team is 2. On clicking this button 2 more text-fields are revealed for Member 3's name and entry number.
  * 'REMOVE MEMBER '-' : Once Add Member button is clicked, the button transforms to the Remove Member button which the user can use to return back to a 2 member team. This hides the text-fields for Member 3's name and entry number.
  * CLEAR : This button wipes out all the text-fields and resets the form.

- We have added a special auto-fill drop down menu feature in order to make user experience more comfortable. When the user starts typing a team member name in any name field, a drop down menu appears listing names of students registered for the course whose names match the entered text. On tapping any name on this list, the name and entry number field are  auto-filled for that student. Also, if the user chooses to enter the entry number first, if it is a valid entry number and belongs to a student registered for COP290, then his/her name gets auto-filled in the name field.

- We have added animation to '+' button which scrolls down setting the third member text-fields visible. Similarly, '-' button scrolls up on tapping removing the third member text-fields with it.

<p align="center">
<img src="/ReadMe Assets/Screenshot3.png" width="300">
<img src="/ReadMe Assets/Screenshot4.png" width="300">
</p>

## Implementation Details

- The app performs the following validation checks on the entered data before sending a request to the server:
  * 'Empty Field' error: This error is flagged if the user submits the team details with one or more text-fields left empty.
  *  'Invalid Entry Number' error: This error is flagged if the entered Entry Number is not valid. The entry number should be of standard IITD format (YEAR + DEPT CODE + SERIAL NO). It accepts all department codes and years from 2000 up to 2015.
  * 'Repeated Entry Number' error:  This error is flagged if the user enters the same entry number for two or more of its team members.

- After successful validity checks on the entered data, the app sends an HTTP POST Request to the server along with the necessary parameters, using the (Android Volley library)[http://developer.android.com/training/volley/index.html]. In case of a successful request, a JSON response is received from the server which is parsed an appropriate message is displayed in an Alert Dialog Box.

- To implement the auto-fill drop down list for names, a database containing student name and entry number information is loaded from a text file. The information has been downloaded from the course mailing list. 
