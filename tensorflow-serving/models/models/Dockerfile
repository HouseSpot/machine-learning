# Use the official TensorFlow Serving image as a base
FROM tensorflow/serving:latest

# Set the model base path environment variable
ENV MODEL_BASE_PATH=/models

# Set the model name environment variable
ENV MODEL_NAME=my_model

# Copy the model to the container
COPY my_model /models/my_model

# Expose the TensorFlow Serving ports
EXPOSE 8500
EXPOSE 8501

# Specify the entrypoint command
ENTRYPOINT ["tensorflow_model_server", "--rest_api_port=8501", "--model_name=my_model", "--model_base_path=/models/my_model"]