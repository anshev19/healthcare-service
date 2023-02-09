import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceTest {
    @Test
    public void bloodPressureAlertTest() {
        PatientInfoRepository repository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(repository.getById(Mockito.anyString()))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("38.2"), new BloodPressure(125, 85))));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(repository, alertService);
        medicalService.checkBloodPressure("", new BloodPressure(120, 80));

        Mockito.verify(alertService, Mockito.times(1)).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: null, need help", argumentCaptor.getValue());
    }

    @Test
    public void temperatureAlertTest() {
        PatientInfoRepository repository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(repository.getById(Mockito.anyString()))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("38.2"), new BloodPressure(125, 85))));
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(repository, alertService);
        medicalService.checkTemperature("", new BigDecimal("36.6"));

        Mockito.verify(alertService, Mockito.times(1)).send(argumentCaptor.capture());
        Assertions.assertEquals("Warning, patient with id: null, need help", argumentCaptor.getValue());
    }

    @Test
    public void dontSendAlertTest() {
        PatientInfoRepository repository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(repository.getById(Mockito.anyString()))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("37.2"), new BloodPressure(120, 80))));
        SendAlertService alertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(repository, alertService);
        medicalService.checkBloodPressure("", new BloodPressure(120, 80));
        medicalService.checkTemperature("", new BigDecimal("36.6"));

        Mockito.verify(alertService, Mockito.times(0)).send(Mockito.anyString());
    }
}
