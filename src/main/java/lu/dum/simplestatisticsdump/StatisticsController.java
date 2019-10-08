package lu.dum.simplestatisticsdump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticsController {

    private static final String filePath = "/tmp/";
    private static final String fileName = "statistics.txt";

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String getAbsoluteFilePath() {
        return filePath + fileName;
    }

    /**
     * Normally that kind of stuff should be done via POST,
     * but in the past we always had last minute problems regarding CORS.
     *
     * So this Endpoint is there as a backup, since GET should result in  less problems.
     */
    @GetMapping("/submit-statistics")
    public String submitStatisticsViaGET(@RequestParam Map<String, Object> queryParameters) {
        updateStatisticsWithIncomingData(queryParameters);
        return "Saved";
    }

    @PostMapping("submit-statistics")
    public String submitStatisticsViaPOST(@RequestBody Map<String, Object> data) {
        updateStatisticsWithIncomingData(data);
        return "Saved";
    }

    private void updateStatisticsWithIncomingData(Map<String, Object> data) {
        GlobalWrapper statistics = readStatisticsFile();

        String currentTimestamp = Instant.now().truncatedTo(ChronoUnit.SECONDS).toString();
        EntryWrapper statisticsEntry = new EntryWrapper(currentTimestamp, data);
        statistics.entries.add(statisticsEntry);

        overwriteStatisticsFile(statistics);
    }

    private GlobalWrapper readStatisticsFile() {
        String fileContent;
        try {
            fileContent = Files.readString(Path.of(getAbsoluteFilePath()));
            return gson.fromJson(fileContent, GlobalWrapper.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new GlobalWrapper(new ArrayList<>());
        }
    }

    private void overwriteStatisticsFile(GlobalWrapper statistics) {
        String json = gson.toJson(statistics);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(getAbsoluteFilePath(), false), StandardCharsets.UTF_8))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AllArgsConstructor
    private class GlobalWrapper {
        private List<EntryWrapper> entries;
    }

    @AllArgsConstructor
    private class EntryWrapper {
        private String timestamp;
        private Map<String, Object> submittedData;
    }
}
