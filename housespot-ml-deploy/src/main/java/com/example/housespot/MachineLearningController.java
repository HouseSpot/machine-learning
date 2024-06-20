package com.example.housespot;

import com.example.housespot.MachineLearningService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MachineLearningController {

    private final MachineLearningService tfServingClient;

    @Autowired
    public MachineLearningController(MachineLearningService tfServingClient) {
        this.tfServingClient = tfServingClient;
    }

    @PostMapping("/predict")
    public Prediction predict(@RequestBody Attribute attribute) throws Exception {
        // Assuming "model" is your TensorFlow Serving model name

        int encodedCity = CityEncoder.encodeCity(attribute.getCity());

        int encodedDistrict = DistrictEncoder.encodeDistrict(attribute.getDistrict());

        List<Float> listFeaturesEncodedScaled = MinMaxScaler.scale(attribute, encodedDistrict, encodedCity);

        String body = createRequestBody(listFeaturesEncodedScaled);

        String response = tfServingClient.predict(body);

        float predictionFloat = getPredictionFromJson(response);

        int roundedPrediction = rounder(predictionFloat);

        String result = CategoryEncoder.decodeCategory(roundedPrediction);

        return new Prediction(result);

    }

    public static int rounder(float realNumber) {
        float secondDecimal = (float) (realNumber * 0.1); // Extract the second decimal place

        if (secondDecimal < 0.5) {
            return (int) Math.floor(realNumber); // Round down using floor function
        } else {
            return (int) Math.ceil(realNumber); // Round up using ceil function
        }
    }

    public static float getPredictionFromJson(String jsonString) {
        // Parse the JSON string
        JSONObject jsonObject = new JSONObject(jsonString);

        // Get the prediction value as float
        float prediction = (float) jsonObject.getJSONArray("predictions")
                .getJSONArray(0)
                .getDouble(0);

        return prediction;
    }

    static String createRequestBody(List<Float> scaledValues) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"instances\": [[");

        for (int i = 0; i < scaledValues.size(); i++) {
            jsonBuilder.append(scaledValues.get(i));
            if (i < scaledValues.size() - 1) {
                jsonBuilder.append(", ");
            }
        }

        jsonBuilder.append("]]}");
        return jsonBuilder.toString();
    }

    static class CategoryEncoder {
        // Static map to store the encoding
        private static final Map<Integer, String> decodingMap = new HashMap<>();

        // Static initialization block to populate the decoding map
        static {
            decodingMap.put(0, "0-250 juta");
            decodingMap.put(1, "250-500 juta");
            decodingMap.put(2, "500-750 juta");
            decodingMap.put(3, "750 juta-1 miliar");
            decodingMap.put(4, "1-1.5 miliar");
            decodingMap.put(5, "1.5-2 miliar");
            decodingMap.put(6, "2-2.5 miliar");
            decodingMap.put(7, "2.5-3 miliar");
            decodingMap.put(8, "3-4 miliar");
            decodingMap.put(9, "4-5 miliar");
            decodingMap.put(10, "lebih dari 5 miliar");
        }

        // Private constructor to prevent instantiation
        private CategoryEncoder() {}

        public static String decodeCategory(int encodedValue) {
            return decodingMap.get(encodedValue);
        }
    }

    static class CityEncoder {
        private static final Map<String, Integer> cityEncoding;

        static {
            cityEncoding = new HashMap<>();
            cityEncoding.put("Jakarta Selatan", 1);
            cityEncoding.put("Jakarta Utara", 2);
            cityEncoding.put("Jakarta Pusat", 3);
            cityEncoding.put("Jakarta Barat", 4);
            cityEncoding.put("Jakarta Timur", 5);
            cityEncoding.put("Tangerang", 6);
            cityEncoding.put("Depok", 7);
            cityEncoding.put("Bogor", 8);
            cityEncoding.put("Bekasi", 9);
        }

        public static int encodeCity(String city) {
            return cityEncoding.getOrDefault(city, -1); // Return -1 if the city is not found
        }

    }


    static class DistrictEncoder {
        private static final Map<String, Integer> districtEncoding;

        static {
            districtEncoding = new HashMap<>();
            districtEncoding.put("Menteng Atas", 1);
            districtEncoding.put("Hasyim Ashari", 2);
            districtEncoding.put("Mega Kuningan", 3);
            districtEncoding.put("Menteng", 4);
            districtEncoding.put("Senayan", 5);
            districtEncoding.put("Scbd", 6);
            districtEncoding.put("Blok M", 7);
            districtEncoding.put("Simprug", 8);
            districtEncoding.put("Permata Hijau", 9);
            districtEncoding.put("Prapanca", 10);
            districtEncoding.put("Slipi", 11);
            districtEncoding.put("Senopati", 12);
            districtEncoding.put("Menteng Dalam", 13);
            districtEncoding.put("Kuningan", 14);
            districtEncoding.put("Patra Kuningan", 15);
            districtEncoding.put("Pakubuwono", 16);
            districtEncoding.put("Sudirman", 17);
            districtEncoding.put("Kebayoran Baru", 18);
            districtEncoding.put("Bangka", 19);
            districtEncoding.put("Pejompongan", 20);
            districtEncoding.put("Pantai Mutiara", 21);
            districtEncoding.put("Ancol", 22);
            districtEncoding.put("Kebon Kacang", 23);
            districtEncoding.put("Pondok Indah", 24);
            districtEncoding.put("Lenteng Agung", 25);
            districtEncoding.put("Bendungan Hilir", 26);
            districtEncoding.put("Setia Budi", 27);
            districtEncoding.put("Setiabudi", 28);
            districtEncoding.put("Fatmawati", 29);
            districtEncoding.put("Ragunan", 30);
            districtEncoding.put("Cipulir", 31);
            districtEncoding.put("Kedoya", 32);
            districtEncoding.put("Cipete", 33);
            districtEncoding.put("Guntur", 34);
            districtEncoding.put("Intercon", 35);
            districtEncoding.put("Ciganjur", 36);
            districtEncoding.put("Mampang Prapatan", 37);
            districtEncoding.put("Pulomas", 38);
            districtEncoding.put("Tanjung Duren Utara", 39);
            districtEncoding.put("Ampera", 40);
            districtEncoding.put("Pantai Indah Kapuk", 41);
            districtEncoding.put("Patal Senayan", 42);
            districtEncoding.put("Panglima Polim", 43);
            districtEncoding.put("Pluit", 44);
            districtEncoding.put("Kebayoran Lama", 45);
            districtEncoding.put("Tawakal", 46);
            districtEncoding.put("Lebak Bulus", 47);
            districtEncoding.put("Kedoya Baru", 48);
            districtEncoding.put("Gajah Mada", 49);
            districtEncoding.put("Tanah Abang", 50);
            districtEncoding.put("Harmoni", 51);
            districtEncoding.put("Pondok Hijau Golf", 52);
            districtEncoding.put("Tanjung Duren Selatan", 53);
            districtEncoding.put("Veteran", 54);
            districtEncoding.put("Angke", 55);
            districtEncoding.put("Pejaten", 56);
            districtEncoding.put("Jati Padang", 57);
            districtEncoding.put("Cilandak", 58);
            districtEncoding.put("Warung Buncit", 59);
            districtEncoding.put("Kemang", 60);
            districtEncoding.put("Pagedangan", 61);
            districtEncoding.put("Tb Simatupang", 62);
            districtEncoding.put("Green Garden", 63);
            districtEncoding.put("Blok S", 64);
            districtEncoding.put("Taman Sari", 65);
            districtEncoding.put("Duren Tiga", 66);
            districtEncoding.put("Sunrise Garden", 67);
            districtEncoding.put("Gambir", 68);
            districtEncoding.put("Permata Buana", 69);
            districtEncoding.put("Villa Meruya", 70);
            districtEncoding.put("Penjaringan", 71);
            districtEncoding.put("Pondok Kelapa", 72);
            districtEncoding.put("Taman Meruya", 73);
            districtEncoding.put("Mampang", 74);
            districtEncoding.put("Petojo", 75);
            districtEncoding.put("Golf Island", 76);
            districtEncoding.put("Kemanggisan", 77);
            districtEncoding.put("Palmerah", 78);
            districtEncoding.put("Citra Garden 2 Extension", 79);
            districtEncoding.put("Cideng", 80);
            districtEncoding.put("Sektor 3 - Bintaro", 81);
            districtEncoding.put("Tebet", 82);
            districtEncoding.put("Muara Karang", 83);
            districtEncoding.put("Alfa Indah", 84);
            districtEncoding.put("Pondok Pinang", 85);
            districtEncoding.put("Jatinegara", 86);
            districtEncoding.put("Pondok Labu", 87);
            districtEncoding.put("Sawah Besar", 88);
            districtEncoding.put("Jembatan Dua", 89);
            districtEncoding.put("Pulo Gadung", 90);
            districtEncoding.put("Citra Grand", 91);
            districtEncoding.put("Radio Dalam", 92);
            districtEncoding.put("Condet", 93);
            districtEncoding.put("Ulujami", 94);
            districtEncoding.put("Kelapa Gading", 95);
            districtEncoding.put("Tambora", 96);
            districtEncoding.put("Taman Mini", 97);
            districtEncoding.put("Taman Grisenda", 98);
            districtEncoding.put("Tomang", 99);
            districtEncoding.put("Sunter", 100);
            districtEncoding.put("Gandaria", 101);
            districtEncoding.put("Puri Mansion", 102);
            districtEncoding.put("Jembatan Lima", 103);
            districtEncoding.put("Cipinang Melayu", 104);
            districtEncoding.put("Kav Dki", 105);
            districtEncoding.put("Teluk Gong", 106);
            districtEncoding.put("Citra Garden", 107);
            districtEncoding.put("Pasar Baru", 108);
            districtEncoding.put("Tanjung Priok", 109);
            districtEncoding.put("Pesanggrahan", 110);
            districtEncoding.put("Pademangan", 111);
            districtEncoding.put("Pasar Minggu", 112);
            districtEncoding.put("Raffles Hills", 113);
            districtEncoding.put("Karang Anyar", 114);
            districtEncoding.put("Klender", 115);
            districtEncoding.put("Grogol Petamburan", 116);
            districtEncoding.put("Green Mansion", 117);
            districtEncoding.put("Pancoran", 118);
            districtEncoding.put("Green Ville", 119);
            districtEncoding.put("Grogol", 120);
            districtEncoding.put("Kalibata", 121);
            districtEncoding.put("Kembangan", 122);
            districtEncoding.put("Joglo", 123);
            districtEncoding.put("Green Lake City", 124);
            districtEncoding.put("Grand Depok City", 125);
            districtEncoding.put("Petukangan", 126);
            districtEncoding.put("Taman Ratu", 127);
            districtEncoding.put("Puri Indah", 128);
            districtEncoding.put("Puri Media", 129);
            districtEncoding.put("Kebagusan", 130);
            districtEncoding.put("Bintaro", 131);
            districtEncoding.put("Tanah Kusir", 132);
            districtEncoding.put("Pantai Indah Kapuk 2", 133);
            districtEncoding.put("Rawamangun", 134);
            districtEncoding.put("Kelapa Dua", 135);
            districtEncoding.put("Metland Puri", 136);
            districtEncoding.put("Kali Deres", 137);
            districtEncoding.put("Pulogebang", 138);
            districtEncoding.put("Duren Sawit", 139);
            districtEncoding.put("Jelambar", 140);
            districtEncoding.put("Karang Tengah", 141);
            districtEncoding.put("Cipedak", 142);
            districtEncoding.put("Duri Kepa", 143);
            districtEncoding.put("Pinang Ranti", 144);
            districtEncoding.put("Kayu Putih", 145);
            districtEncoding.put("Mangga Besar", 146);
            districtEncoding.put("Kebon Jeruk", 147);
            districtEncoding.put("L'Agricola", 148);
            districtEncoding.put("Johar Baru", 149);
            districtEncoding.put("Cipinang", 150);
            districtEncoding.put("Kayu Jati", 151);
            districtEncoding.put("Makasar", 152);
            districtEncoding.put("Kalideres", 153);
            districtEncoding.put("Cisarua", 154);
            districtEncoding.put("Bukit Duri", 155);
            districtEncoding.put("Tanjung Barat", 156);
            districtEncoding.put("Pondok Bambu", 157);
            districtEncoding.put("Cempaka Putih", 158);
            districtEncoding.put("Duta Garden", 159);
            districtEncoding.put("Tanjung Duren", 160);
            districtEncoding.put("Legenda Wisata", 161);
            districtEncoding.put("Cengkareng", 162);
            districtEncoding.put("Taman Surya", 163);
            districtEncoding.put("Meruya", 164);
            districtEncoding.put("Koja", 165);
            districtEncoding.put("Cinere", 166);
            districtEncoding.put("Cakung", 167);
            districtEncoding.put("Senen", 168);
            districtEncoding.put("Jagakarsa", 169);
            districtEncoding.put("Taman Palem", 170);
            districtEncoding.put("Daan Mogot", 171);
            districtEncoding.put("Cengkareng Barat", 172);
            districtEncoding.put("Semanan", 173);
            districtEncoding.put("Buaran", 174);
            districtEncoding.put("Pegangsaan", 175);
            districtEncoding.put("Kramat Jati", 176);
            districtEncoding.put("Bojong Indah", 177);
            districtEncoding.put("Duri Kosambi", 178);
            districtEncoding.put("Sektor 2 - Bintaro", 179);
            districtEncoding.put("Larangan", 180);
            districtEncoding.put("Kota Wisata", 181);
            districtEncoding.put("Sindang Jaya", 182);
            districtEncoding.put("Babakan Madang", 183);
            districtEncoding.put("Cirendeu", 184);
            districtEncoding.put("Utan Kayu", 185);
            districtEncoding.put("Kemayoran", 186);
            districtEncoding.put("Benda", 187);
            districtEncoding.put("Cibodas", 188);
            districtEncoding.put("Cawang", 189);
            districtEncoding.put("Matraman", 190);
            districtEncoding.put("Pisangan Lama", 191);
            districtEncoding.put("Cempaka Mas", 192);
            districtEncoding.put("Cilangkap", 193);
            districtEncoding.put("Bambu Apus", 194);
            districtEncoding.put("Sumur Batu", 195);
            districtEncoding.put("Cibubur", 196);
            districtEncoding.put("Bogor Tengah", 197);
            districtEncoding.put("Tangerang", 198);
            districtEncoding.put("Cinangka", 199);
            districtEncoding.put("Kosambi", 200);
            districtEncoding.put("Graha Bintaro", 201);
            districtEncoding.put("Cipondoh", 202);
            districtEncoding.put("Cilincing", 203);
            districtEncoding.put("Daan Mogot Arcadia", 204);
            districtEncoding.put("Kresek", 205);
            districtEncoding.put("Batuceper", 206);
            districtEncoding.put("Ciledug", 207);
            districtEncoding.put("Bogor Utara", 208);
            districtEncoding.put("Bsd City", 209);
            districtEncoding.put("Cimanggis", 210);
            districtEncoding.put("Pondokmelati", 211);
            districtEncoding.put("Bogor Selatan", 212);
            districtEncoding.put("Pondokgede", 213);
            districtEncoding.put("Pasar Rebo", 214);
            districtEncoding.put("Limo", 215);
            districtEncoding.put("Jatisampurna", 216);
            districtEncoding.put("Ciawi", 217);
            districtEncoding.put("Pinang", 218);
            districtEncoding.put("Gunung Putri", 219);
            districtEncoding.put("Curug", 220);
            districtEncoding.put("Pondok Gede", 221);
            districtEncoding.put("Caringin", 222);
            districtEncoding.put("Neglasari", 223);
            districtEncoding.put("Taman Parahyangan", 224);
            districtEncoding.put("Cikupa", 225);
            districtEncoding.put("Dramaga", 226);
            districtEncoding.put("Ciracas", 227);
            districtEncoding.put("Bogor Timur", 228);
            districtEncoding.put("Jatiwaringin", 229);
            districtEncoding.put("Percetakan Negara", 230);
            districtEncoding.put("Teluknaga", 231);
            districtEncoding.put("Balaraja", 232);
            districtEncoding.put("Salemba", 233);
            districtEncoding.put("Beji", 234);
            districtEncoding.put("Sukma Jaya", 235);
            districtEncoding.put("Jatiasih", 236);
            districtEncoding.put("Jawa Barat", 237);
            districtEncoding.put("Cijantung", 238);
            districtEncoding.put("Kalisari", 239);
            districtEncoding.put("Mauk", 240);
            districtEncoding.put("Tapos", 241);
            districtEncoding.put("Karawaci", 242);
            districtEncoding.put("Kedungwaringin", 243);
            districtEncoding.put("Bojongsari", 244);
            districtEncoding.put("Tarumajaya", 245);
            districtEncoding.put("Bogor Barat", 246);
            districtEncoding.put("Pancoran Mas", 247);
            districtEncoding.put("Cilodong", 248);
            districtEncoding.put("Tanah Sereal", 249);
            districtEncoding.put("Medan Satria", 250);
            districtEncoding.put("Bekasi Barat", 251);
            districtEncoding.put("Periuk", 252);
            districtEncoding.put("Cibinong", 253);
            districtEncoding.put("Panongan", 254);
            districtEncoding.put("Bekasi Selatan", 255);
            districtEncoding.put("Sawangan", 256);
            districtEncoding.put("Legok", 257);
            districtEncoding.put("Bali Resort Bogor", 258);
            districtEncoding.put("Pasarkemis", 259);
            districtEncoding.put("Cikarang Timur", 260);
            districtEncoding.put("Pondok Ranggon", 261);
            districtEncoding.put("Cikoko", 262);
            districtEncoding.put("Cileungsi", 263);
            districtEncoding.put("Cipayung", 264);
            districtEncoding.put("Leuwisadeng", 265);
            districtEncoding.put("Tambun Selatan", 266);
            districtEncoding.put("Cikarang Pusat", 267);
            districtEncoding.put("Rawalumbu", 268);
            districtEncoding.put("Ciomas", 269);
            districtEncoding.put("Bekasi Timur", 270);
            districtEncoding.put("Cikupa Citra Raya", 271);
            districtEncoding.put("Leuwiliang", 272);
            districtEncoding.put("Bekasi Utara", 273);
            districtEncoding.put("Bojonggede", 274);
            districtEncoding.put("Mustikajaya", 275);
            districtEncoding.put("Cikarang Selatan", 276);
            districtEncoding.put("Cikarang Barat", 277);
            districtEncoding.put("Gunung Sindur", 278);
            districtEncoding.put("Tamansari", 279);
            districtEncoding.put("Tajurhalang", 280);
            districtEncoding.put("Jati Uwung", 281);
            districtEncoding.put("Bantargebang", 282);
            districtEncoding.put("Megamendung", 283);
            districtEncoding.put("Tigaraksa", 284);
            districtEncoding.put("Bojong Gede", 285);
            districtEncoding.put("Sukaraja", 286);
            districtEncoding.put("Parung", 287);
            districtEncoding.put("Cikarang Utara", 288);
            districtEncoding.put("Bungur", 289);
            districtEncoding.put("Gunung Sahari", 290);
            districtEncoding.put("Jatibening", 291);
            districtEncoding.put("Tambun Utara", 292);
            districtEncoding.put("Jayanti", 293);
            districtEncoding.put("Babelan", 294);
            districtEncoding.put("Parung Panjang", 295);
            districtEncoding.put("Sepatan", 296);
            districtEncoding.put("Setu", 297);
            districtEncoding.put("Rumpin", 298);
            districtEncoding.put("Ranca Bungur", 299);
            districtEncoding.put("Cijeruk", 300);
            districtEncoding.put("Cibungbulang", 301);
            districtEncoding.put("Tajur Halang", 302);
            districtEncoding.put("Cisauk", 303);
            districtEncoding.put("Rajeg", 304);
            districtEncoding.put("Jonggol", 305);
            districtEncoding.put("Citeureup", 306);
            districtEncoding.put("Pakuhaji", 307);
            districtEncoding.put("Sukawangi", 308);
            districtEncoding.put("Sepatan Timur", 309);
            districtEncoding.put("Cibitung", 310);
            districtEncoding.put("Ciampea", 311);
            districtEncoding.put("Klapanunggal", 312);
            districtEncoding.put("Ciseeng", 313);
            districtEncoding.put("Sukatani", 314);
            districtEncoding.put("Karangbahagia", 315);
            districtEncoding.put("Cibarusah", 316);
            districtEncoding.put("Tenjo", 317);
            districtEncoding.put("Jambe", 318);
            districtEncoding.put("Sukadiri", 319);
            districtEncoding.put("Pamijahan", 320);
            districtEncoding.put("Cariu", 321);
            districtEncoding.put("Solear", 322);
            districtEncoding.put("Serang Baru", 323);
            districtEncoding.put("Cisoka", 324);
            districtEncoding.put("Jasinga", 325);
            districtEncoding.put("Kelapa Nunggal", 326);
        }

        public static int encodeDistrict(String district) {
            return districtEncoding.getOrDefault(district, -1); // Return -1 if the city is not found
        }

    }

    static class MinMaxScaler {

        // Private constructor to prevent instantiation
        private MinMaxScaler() {
            // Private constructor to prevent instantiation of this utility class
        }

        // Hardcoded min-max values for each attribute
        private static final float DISTRICT_MIN = 1.0f;
        private static final float DISTRICT_MAX = 326.0f;
        private static final float CITY_MIN = 1.0f;
        private static final float CITY_MAX = 9.0f;
        private static final float LATITUDE_MIN = -7.390627384185791f;
        private static final float LATITUDE_MAX = 0.0074957683468534f;
        private static final float LONGITUDE_MIN = 0.009927861392498f;
        private static final float LONGITUDE_MAX = 107.5799956f;
        private static final float LAND_SIZE_MIN = 20.0f;
        private static final float LAND_SIZE_MAX = 9632.0f;
        private static final float BUILDING_SIZE_MIN = 21.0f;
        private static final float BUILDING_SIZE_MAX = 4461.0f;
        private static final float FLOORS_MIN = 1.0f;
        private static final float FLOORS_MAX = 14.0f;
        private static final float BEDROOMS_MIN = 1.0f;
        private static final float BEDROOMS_MAX = 56.0f;
        private static final float BATHROOMS_MIN = 1.0f;
        private static final float BATHROOMS_MAX = 56.0f;
        private static final float CARPORT_GARAGE_MIN = 0.0f;
        private static final float CARPORT_GARAGE_MAX = 15.0f;

        // Min-Max scaling for a single attribute object, returning a list of scaled values
        public static List<Float> scale(Attribute attribute, int encodedDistrict, int encodedCity) {
            List<Float> scaledValues = new ArrayList<>();

            // Scale each attribute based on hardcoded min-max values and add to the list
            scaledValues.add(scaleValue((float) encodedDistrict, DISTRICT_MIN, DISTRICT_MAX));
            scaledValues.add(scaleValue((float) encodedCity, CITY_MIN, CITY_MAX));
            scaledValues.add(scaleValue(attribute.getLatitude(), LATITUDE_MIN, LATITUDE_MAX));
            scaledValues.add(scaleValue(attribute.getLongitude(), LONGITUDE_MIN, LONGITUDE_MAX));
            scaledValues.add(scaleValue(attribute.getLandSize(), LAND_SIZE_MIN, LAND_SIZE_MAX));
            scaledValues.add(scaleValue(attribute.getBuildingSize(), BUILDING_SIZE_MIN, BUILDING_SIZE_MAX));
            scaledValues.add(scaleValue((float) attribute.getFloors(), FLOORS_MIN, FLOORS_MAX));
            scaledValues.add(scaleValue((float) attribute.getBedrooms(), BEDROOMS_MIN, BEDROOMS_MAX));
            scaledValues.add(scaleValue((float) attribute.getBathrooms(), BATHROOMS_MIN, BATHROOMS_MAX));
            scaledValues.add(scaleValue((float) attribute.getCarportGarage(), CARPORT_GARAGE_MIN, CARPORT_GARAGE_MAX));

            return scaledValues;
        }

        // Helper method to scale a single attribute value
        private static float scaleValue(float value, float min, float max) {
            if (max - min == 0) {
                return 0.0f; // Avoid division by zero
            }
            return (value - min) / (max - min);
        }
    }

}