# caliber-email-service
Microservice that provides Email functionality for Caliber application

# required environment variables
CALIBER_CONFIG_REPO_URL

# API endpoints

## /emails/getSchedule?email_type=[trainerGradeReminder, vpBatchStatusReport] <br />
  GET Method <br />
  Retreive infomation about the current scheduled email service <br />
  return json {delay: , interval: } <br />

## /emails/getTrainers?email_type=[trainerGradeReminder, vpBatchStatusReport] <br />
  GET Method <br />
  Get a list of trainers emails are sent to <br />
  return json {[Trainer*]}  list of trainers <br />
  
## /emails/startSchedule 
  POST Method <br />
  Required in the request body:  <br />
  { <br />
    email_type: <br />
    interval: <br />
    delay: <br />
  } <br />
  

## /emails/send/{id} 
  POST Method <br />
  Send an email directly to an employee with given id in url <br />
  not currently being used <br />
  
