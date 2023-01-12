package kawa.pyszna.grading;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grading")
@RequiredArgsConstructor
public class Grader {

    @GetMapping()
    public ResponseEntity<GradeDTO> getGrading(@RequestParam List<Double> coords) {
        Double x = coords.get(0);
        Double y = coords.get(1);

        return ResponseEntity.ok().body(grade(x, y));
    }

    public GradeDTO grade(Double x, Double y) {
        // tu od kawsone trzeba
        Map<String, List<Place>> categoryMap = new HashMap<>(Map.of("kino", List.of(new Place(1), new Place(2)), "teatr", List.of(new Place(1))));

        Map<String, Integer> gradeMap = new HashMap<>();

        for (Map.Entry<String, List<Place>> entry : categoryMap.entrySet()) {
            Integer grade = entry.getValue().stream()
                    .min((Place p1, Place p2) -> getValue(entry.getKey(), p1.time).compareTo(getValue(entry.getKey(), p2.time)))
                    .map(p -> getValue(entry.getKey(), p.time))
                    .orElse(0);
            gradeMap.put(entry.getKey(), grade);
        }

        //tu kategorie dobrac
        return GradeDTO.builder()
                .kino(gradeMap.get("kino"))
                .teatr(gradeMap.get("teatr"))
                .build();
    }

    private Integer getValue(String category, Integer time) {
        Integer wage = switch (category) {
            case "kino" -> 1;
            case "teatr" -> 5;
            default -> 0;
        };
        return (15*60-time)*wage;
    }
}
