We mainly run all the notebook in here in Visual Studio Code but sometimes we use Google Collab. To know which you need to run on Visual Studio Code if the notebook contains terminal command that starts with % otherwise if the terminal command starts with ! you need to run it in Google Collab. And also please don't forget to install python and the packages needed to run the notebook.

The 99Co_Scrapping folder is for scrapping data from 99.co/id to get house prices and it's appropriate features. Run the notebook to scrap the data from 99.co/id and adjust accordingly if you want another case because for our case we scrap only from the latest house and in Jabodetabek and with our selected features. The output of this scrapping will be in .csv format.

The Data_Preprocessing folder is for processing data from scrapped data from the internet. Also there are to notebook in there, one for processing raw data from scrapped data and one for merging the processed data from before because we do the scraping batchly not in one go. Run the One_On_One notebook first to process the individual dataset and adjust accordingly if you want different kinds of preprocessing and after that run the To_One to merge the data to one dataset.

The Dataset folder contains the processed and unprocessed data. 

The housespot-ml-deploy folder contain Java SpringBoot that consume RESTs API from tensorflow serving and output the appropriate output. You can use CloudRun to deploy the Java SpringBoot by specificying the path of the Dockerfile.

The ML_Model contains the machine learning model maker with the output of .h5 and the tensorflow own output that you can use with tensorflow serving To achieve this you can run the ML_Generator notebook. There are also Converter_H5_To_JSON_and_BIN that convert .h5 to readeable format by javascript. js_ml_deploy is an example on how to use the model in JavaScript.

The Rumah123_Scrapping folder is for scrapping data from rumah123.com to get house prices and it's appropriate features. Run the notebook to scrap the data from 99.co/id and adjust accordingly if you want another case because for our case we scrap only from the latest house and in Jabodetabek and with our selected features. The output of this scrapping will be in .csv format.

The tensorflow-serving folder contains dockerfile and the saved model. You can deploy the model with CloudRun with specifying the path to the Dockerfile.