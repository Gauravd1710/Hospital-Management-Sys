package model;

public class PatientDTO {
    private long id;

    private String patientId;
    private String name;
    private String diagnosis;
    private String type;
    private String details;

    public PatientDTO(
            String patientId,
            String name,
            String diagnosis,
            String type,
            String details
    ) {
        this.patientId = patientId;
        this.name = name;
        this.diagnosis = diagnosis;
        this.type = type;
        this.details = details;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }
    public long getId() {
    return id;
}

public void setId(long id) {
    this.id = id;
}
}