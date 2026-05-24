package com.hospital.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.backend.entity.Patient;
import com.hospital.backend.repository.PatientRepository;

@RestController
@RequestMapping("/patients")
@CrossOrigin("*")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    public Patient addPatient(
            @RequestBody Patient patient
    ) {

        System.out.println("Received Patient:");
        System.out.println(patient.getPatientId());
        System.out.println(patient.getName());

        return patientRepository.save(patient);
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
}
    @DeleteMapping("/{id}")
    public void deletePatient(
            @PathVariable Long id
    ) {

        patientRepository.deleteById(id);
    }
    @PutMapping("/{id}")
public Patient updatePatient(
        @PathVariable Long id,
        @RequestBody Patient updatedPatient
) {

    Optional<Patient> optionalPatient =
            patientRepository.findById(id);

    if (optionalPatient.isPresent()) {

        Patient patient =
                optionalPatient.get();

        patient.setPatientId(
                updatedPatient.getPatientId()
        );

        patient.setName(
                updatedPatient.getName()
        );

        patient.setDiagnosis(
                updatedPatient.getDiagnosis()
        );

        patient.setType(
                updatedPatient.getType()
        );

        patient.setDetails(
                updatedPatient.getDetails()
        );

        return patientRepository.save(patient);
    }

    return null;
}
}




// package com.hospital.backend.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.hospital.backend.entity.Patient;
// import com.hospital.backend.repository.PatientRepository;

// @RestController
// @RequestMapping("/patients")
// @CrossOrigin("*")
// public class PatientController {

//     @Autowired
//     private PatientRepository patientRepository;

//     @PostMapping
//     public Patient addPatient(
//             @RequestBody Patient patient
//     ) {
//         return patientRepository.save(patient);
//     }
// }