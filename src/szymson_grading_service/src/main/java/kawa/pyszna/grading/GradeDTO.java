package kawa.pyszna.grading;

import lombok.Builder;

@Builder
public record GradeDTO(
        Integer kino,
        Integer teatr
) {
}
