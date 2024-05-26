package stepper.flow.excution.context;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StepsLog implements Serializable
{
        private String dataLog;
        private String creationLogTime;

        public StepsLog(String dataLog)
        {
            this.dataLog=dataLog;
            this.creationLogTime=setCreationLogTime(LocalTime.now());
        }
        public String setCreationLogTime(LocalTime time)
        {
            // Format the time in HH:MM:SS format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = time.format(formatter);

            return formattedTime;
        }

        public String getCreationLogTime() {
            return creationLogTime;
        }

        public String getDataLog() {
            return dataLog;
        }
    }


