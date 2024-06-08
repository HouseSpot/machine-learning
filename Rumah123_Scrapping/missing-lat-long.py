import pandas as pd
import googlemaps

# Your Google Maps API key
api_key = 'AIzaSyDtLohDGDggrI8quTh7wDFHh-O-I3SH6oY'
gmaps = googlemaps.Client(key=api_key)

def get_lat_long(address, gmaps):
    geocode_result = gmaps.geocode(address)
    if geocode_result:
        location = geocode_result[0]['geometry']['location']
        return location['lat'], location['lng']
    else:
        return ("Coordinates not found", "Coordinates not found")

# Load the CSV data
df = pd.read_csv('/Users/salmadanu/Desktop/Bangkit/HouseSpot/HouseSpot-ML/Rumah123_Scrapping/CSV_Files/tangerang-2.csv')

# Iterate through the rows and update the 'latitude' and 'longitude' columns where they are 'unknown'
for index, row in df.iterrows():
    if row['latitude'] == 'unknown' or row['longitude'] == 'unknown':
        address = row['address']
        lat, lng = get_lat_long(address, gmaps)
        df.at[index, 'latitude'] = lat
        df.at[index, 'longitude'] = lng

# Save the updated DataFrame back to a CSV file
df.to_csv('/Users/salmadanu/Desktop/Bangkit/HouseSpot/HouseSpot-ML/Rumah123_Scrapping/CSV_Files/FIX-tangerang-2.csv', index=False)
