const tf = require('@tensorflow/tfjs-node');
const fs = require('fs').promises;

async function loadModel() {
  const model = await tf.loadLayersModel('file://model.json');
  console.log('Model loaded successfully');

   // Prepare a sample input tensor (e.g., random data or a loaded image)
  // Here we create a tensor of shape [1, 224, 224, 3] with random values
  // Replace this with actual image data as needed

  const input = ['Babelan', 'Bekasi', -6.2277207, 107.0016494, 60, 40, 1, 2, 1, 1]

  const districtData = await fs.readFile('encoding_district_dict.json', 'utf8');
  const districtMapping = JSON.parse(districtData);

  input[0] = districtMapping[input[0]];

  const cityData = await fs.readFile('encoding_city_dict.json', 'utf8');
  const cityMapping = JSON.parse(cityData);

  input[1] = cityMapping[input[1]];

  // Read and parse min-max values JSON file
  const minMaxData = await fs.readFile('max_min_values.json', 'utf8');
  const minMaxValues = JSON.parse(minMaxData);

  // Function to apply min-max scaling
  function minMaxScale(value, min, max) {
    return (value - min) / (max - min);
  }

  // Extract numerical values and apply min-max scaling
  const scaledInput = [
    minMaxScale(input[0], minMaxValues.District_min, minMaxValues.District_max),
    minMaxScale(input[1], minMaxValues.City_min, minMaxValues.City_max),
    minMaxScale(input[2], minMaxValues.Latitude_min, minMaxValues.Latitude_max),
    minMaxScale(input[3], minMaxValues.Longitude_min, minMaxValues.Longitude_max),
    minMaxScale(input[4], minMaxValues.Land_Size_M2_min, minMaxValues.Land_Size_M2_max),
    minMaxScale(input[5], minMaxValues.Building_Size_M2_min, minMaxValues.Building_Size_M2_max),
    minMaxScale(input[6], minMaxValues.Floors_min, minMaxValues.Floors_max),
    minMaxScale(input[7], minMaxValues.Bedrooms_min, minMaxValues.Bedrooms_max),
    minMaxScale(input[8], minMaxValues.Bathrooms_min, minMaxValues.Bathrooms_max),
    minMaxScale(input[9], minMaxValues["Carport/Garage_min"], minMaxValues["Carport/Garage_max"])
  ];

  console.log('Input after transformation:', scaledInput);

  const inputTensor = tf.tensor2d([scaledInput], [1, 10]);

  // Make a prediction
  const predictions = model.predict(inputTensor);
  
  // If predictions is a tensor, you might want to use .data() or .array() to extract the data
  const predictionArray = predictions.arraySync(); // or .dataSync() for a flat array
  console.log('Predictions:', predictionArray);

  // Process the predictions (e.g., find the class with the highest score)
  const highestPrediction = predictionArray[0].indexOf(Math.max(...predictionArray[0]));
  console.log('Predicted class:', highestPrediction);

  const priceCategory = await fs.readFile('price_category_mapping.json', 'utf8');
  const priceCategoryMapping = JSON.parse(priceCategory);

  // Process the predictions (e.g., find the class with the highest score)
  const output = priceCategoryMapping[highestPrediction.toString()];
  console.log('Predicted class transformed:', output);
  
}

loadModel();