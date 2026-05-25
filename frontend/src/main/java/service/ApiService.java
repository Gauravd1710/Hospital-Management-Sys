package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.PatientDTO;

public class ApiService {

    private static final String API_URL
            = "http://localhost:8080/patients";

    public static boolean savePatient(PatientDTO patient) {

        try {

            URL url = new URL(API_URL);

            HttpURLConnection conn
                    = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            conn.setDoOutput(true);

            String json = String.format(
                    """
                    {
                        "patientId":"%s",
                        "name":"%s",
                        "diagnosis":"%s",
                        "type":"%s",
                        "details":"%s"
                    }
                    """,
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getDiagnosis(),
                    patient.getType(),
                    patient.getDetails()
            );

            System.out.println(json);

            OutputStream os = conn.getOutputStream();

            os.write(json.getBytes());

            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            System.out.println("Response Code: " + responseCode);

            return responseCode == 200
                    || responseCode == 201;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public static List<PatientDTO> getAllPatients() {

        List<PatientDTO> patients = new ArrayList<>();

        try {

            URL url = new URL(API_URL);

            HttpURLConnection conn
                    = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

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
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return patients;
    }

    public static boolean deletePatient(
            long id
    ) {

        try {

            URL url
                    = new URL(
                            API_URL + "/" + id
                    );

            HttpURLConnection conn
                    = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");

            int responseCode
                    = conn.getResponseCode();

            System.out.println(
                    "DELETE Response Code: "
                    + responseCode
            );

            return responseCode == 200
                    || responseCode == 204;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public static boolean updatePatient(
            long id,
            PatientDTO patient
    ) {

        try {

            ObjectMapper mapper =
                    new ObjectMapper();

            String json =
                    mapper.writeValueAsString(patient);

            URL url =
                    new URL(
                            API_URL + "/" + id
                    );

            HttpURLConnection conn =
                    (HttpURLConnection)
                            url.openConnection();

            conn.setRequestMethod("PUT");

            conn.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            conn.setDoOutput(true);

            OutputStream os =
                    conn.getOutputStream();

            os.write(json.getBytes());

            os.flush();
            os.close();

            int responseCode =
                    conn.getResponseCode();

            System.out.println(
                    "PUT Response Code: "
                            + responseCode
            );

            return responseCode == 200;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}
