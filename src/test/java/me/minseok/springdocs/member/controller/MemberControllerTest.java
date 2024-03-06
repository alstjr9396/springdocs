package me.minseok.springdocs.member.controller;

import static me.minseok.springdocs.global.restdocs.RestDocsConfig.field;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import me.minseok.springdocs.global.ControllerTest;
import me.minseok.springdocs.member.dto.MemberRequest;
import me.minseok.springdocs.member.dto.MemberResponse;
import me.minseok.springdocs.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MemberControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    private void makeMember() throws Exception {
        final MemberRequest tripCreateRequest = new MemberRequest(1L, "gildong");

        when(memberService.saveMember(any(MemberRequest.class))).thenReturn(1L);

        performPostRequest(tripCreateRequest);
    }

    private ResultActions performDeleteRequest() throws Exception {
        return mockMvc.perform(delete("/members/{memberId}", 1)
                .contentType(APPLICATION_JSON));
    }

    private ResultActions performPostRequest(MemberRequest memberRequest) throws Exception {
        return mockMvc.perform(post("/members")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)));
    }

    private ResultActions performGetRequest(int memberId) throws Exception {
        return mockMvc.perform(get("/members/{memberId}", memberId)
                .contentType(APPLICATION_JSON));
    }

    private ResultActions performGetRequest() throws Exception {
        return mockMvc.perform(get("/members")
                .contentType(APPLICATION_JSON));
    }

    @DisplayName("새로운 멤버를 추가한다.")
    @Test
    void addMember() throws Exception {
        // given
        MemberRequest memberRequest = new MemberRequest(1L, "gildong");

        when(memberService.saveMember(any(MemberRequest.class))).thenReturn(1L);

        // when
        ResultActions resultActions = performPostRequest(memberRequest);

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/members/1"))
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("id")
                                                .type(JsonFieldType.NUMBER)
                                                .description("멤버의 ID")
                                                .attributes(field("constraint", "양의 정수")),
                                        fieldWithPath("name")
                                                .type(JsonFieldType.STRING)
                                                .description("멤버의 이름")
                                                .attributes(field("constraint", "문자열"))
                                ),
                                responseHeaders(
                                        headerWithName(LOCATION).description("생성된 멤버 URL")
                                )
                        )
                );
    }

    @DisplayName("memberId로 특정 멤버를 조회한다.")
    @Test
    void getMember() throws Exception {
        // given
        makeMember();
        when(memberService.getMember(1L))
                .thenReturn(new MemberResponse(1L, "gildong"));

        // when
        ResultActions resultActions = performGetRequest(1);

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("memberId")
                                        .description("멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("멤버 ID")
                                        .attributes(field("constraint", "양의 정수")),
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("멤버 이름")
                                        .attributes(field("constraint", "문자열"))
                        )
                ))
                .andReturn();

        MemberResponse memberResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                MemberResponse.class
        );
        assertThat(memberResponse).usingRecursiveComparison()
                .isEqualTo(new MemberResponse(1L, "gildong"));
    }

    @DisplayName("모든 멤버를 조회한다.")
    @Test
    void getAllMembers() throws Exception {
        // given
        makeMember();
        when(memberService.getAllMembers())
                .thenReturn(List.of(new MemberResponse(1L, "gildong")));

        // when
        ResultActions resultActions = performGetRequest();

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("멤버 ID")
                                        .attributes(field("constraint", "양의 정수")),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("멤버 이름")
                                        .attributes(field("constraint", "문자열"))
                        )
                ))
                .andReturn();

        List<MemberResponse> memberResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        assertThat(memberResponses).usingRecursiveComparison()
                .isEqualTo(List.of(new MemberResponse(1L, "gildong")));
    }

    @DisplayName("멤버를 삭제한다.")
    @Test
    void deleteMember() throws Exception {
        // given
        makeMember();

        // when
        ResultActions resultActions = performDeleteRequest();

        // then
        verify(memberService).deleteMember(anyLong());

        resultActions.andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("memberId")
                                        .description("멤버 ID")
                        )
                ));
    }
}