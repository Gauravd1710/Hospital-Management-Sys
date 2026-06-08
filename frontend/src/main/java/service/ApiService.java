package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.PatientDTO;

public class ApiService {

    private static final String API_URL
            = System.getProperty(
                    "hospital.api.url",
                    System.getenv().getOrDefault(
                            "HOSPITAL_API_URL",
                            "https://hospital-backend1-2dav.onrender.com/patients"
                    )
            );

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static String lastError = "";

    public static String getLastError() {
        return lastError;
    }

    public static boolean savePatient(PatientDTO patient) {

        try {
            lastError = "";

            URL url = new URL(API_URL);

            HttpURLConnection conn
                    = (HttpURLConnection) url.openConnection();

            configureJsonConnection(conn);

            conn.setRequestMethod("POST");

            conn.setDoOutput(true);

            String json = MAPPER.writeValueAsString(
                    patientPayload(patient)
            );

            OutputStream os = conn.getOutputStream();

            os.write(json.getBytes(StandardCharsets.UTF_8));

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            System.out.println("Response Code: " + responseCode);

            if (!isSuccess(responseCode)) {
                lastError = readResponseBody(conn);
            }

            return responseCode == 200
                    || responseCode == 201;

        } catch (Exception e) {

            e.printStackTrace();
            lastError = e.getClass().getSimpleName()
                    + ": " + e.getMessage();

            return false;
        }
    }

    public static List<PatientDTO> getAllPatients() {

        List<PatientDTO> patients = new ArrayList<>();

        try {
            lastError = "";

            URL url = new URL(API_URL);

            HttpURLConnection conn
                    = (HttpURLConnection) url.openConnection();

            configureJsonConnection(conn);

            conn.setRequestMethod("GET");

            int responseCode
                    = conn.getResponseCode();

            System.out.println(
                    "GET Response Code: " + responseCode
            );

            if (responseCode == 200) {

                BufferedReader br
                        = new BufferedReader(
                                new InputStreamReader(
                                        conn.getInputStream()
                                )
                        );

                StringBuilder response
                        = new StringBuilder();

                String line;

                while ((line = br.readLine()) != null) {

                    response.append(line);
                }

                br.close();

                System.out.println(
                        "GET Response: " + response
                );

                JSONArray jsonArray = new JSONArray(
                        response.toString()
                );

                for (int i = 0;
                        i < jsonArray.length();
                        i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    PatientDTO patient
                            = new PatientDTO(
                                    obj.getString("patientId"),
                                    obj.getString("name"),
                                    obj.getString("diagnosis"),
                                    obj.getString("type"),
                                    obj.getString("details")
                            );

                    patient.setId(obj.getLong("id"));

                    patients.add(patient);
                }
            } else {
                lastError = readResponseBody(conn);
            }

        } catch (Exception e) {

            e.printStackTrace();
            lastError = e.getClass().getSimpleName()
                    + ": " + e.getMessage();
        }

        return patients;
    }

    public static boolean deletePatient(
            long id
    ) {

        try {
            lastError = "";

            URL url
                    = new URL(
                            API_URL + "/" + id
                    );

            HttpURLConnection conn
                    = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);

            conn.setRequestMethod("DELETE");

            int responseCode
                    = conn.getResponseCode();

            System.out.println(
                    "DELETE Response Code: "
                    + responseCode
            );

            if (!isSuccess(responseCode)) {
                lastError = readResponseBody(conn);
            }

            return responseCode == 200
                    || responseCode == 204;

        } catch (Exception e) {

            e.printStackTrace();
            lastError = e.getClass().getSimpleName()
                    + ": " + e.getMessage();

            return false;
        }
    }

    public static boolean updatePatient(
            long id,
            PatientDTO patient
    ) {

        try {
            lastError = "";

            String json =
                    MAPPER.writeValueAsString(
                            patientPayload(patient)
                    );

            URL url =
                    new URL(
                            API_URL + "/" + id
                    );

            HttpURLConnection conn =
                    (HttpURLConnection)
                            url.openConnection();

            configureJsonConnection(conn);

            conn.setRequestMethod("PUT");

            conn.setDoOutput(true);

            OutputStream os =
                    conn.getOutputStream();

            os.write(json.getBytes(StandardCharsets.UTF_8));

            os.flush();
            os.close();

            int responseCode =
                    conn.getResponseCode();

            System.out.println(
                    "PUT Response Code: "
                            + responseCode
            );

            if (!isSuccess(responseCode)) {
                lastError = readResponseBody(conn);
            }

            return responseCode == 200;

        } catch (Exception e) {

            e.printStackTrace();
            lastError = e.getClass().getSimpleName()
                    + ": " + e.getMessage();

            return false;
        }
    }

    private static void configureJsonConnection(
            HttpURLConnection conn
    ) {
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(60000);
        conn.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8"
        );
        conn.setRequestProperty(
                "Accept",
                "application/json"
        );
    }

    private static Map<String, String> patientPayload(
            PatientDTO patient
    ) {
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("patientId", patient.getPatientId());
        payload.put("name", patient.getName());
        payload.put("diagnosis", patient.getDiagnosis());
        payload.put("type", patient.getType());
        payload.put("details", patient.getDetails());
        return payload;
    }

    private static boolean isSuccess(int responseCode) {
        return responseCode >= 200
                && responseCode < 300;
    }

    private static String readResponseBody(
            HttpURLConnection conn
    ) {
        try {
            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    conn.getErrorStream() != null
                                            ? conn.getErrorStream()
                                            : conn.getInputStream(),
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();

            if (response.isEmpty()) {
                return "HTTP " + conn.getResponseCode();
            }

            return "HTTP " + conn.getResponseCode()
                    + ": " + response;

        } catch (Exception e) {
            return e.getClass().getSimpleName()
                    + ": " + e.getMessage();
        }
    }
}
