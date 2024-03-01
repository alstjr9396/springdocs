package me.minseok.springdocs.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRequest {

    private Long id;

    @NotBlank(message = "이름은 공백이 될 수 없습니다.")
    @Size(max = 10, message = "이름은 10자를 초과할 수 없습니다.")
    private String name;
}
