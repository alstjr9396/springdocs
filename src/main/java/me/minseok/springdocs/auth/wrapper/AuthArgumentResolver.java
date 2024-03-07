package me.minseok.springdocs.auth.wrapper;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.minseok.springdocs.auth.Auth;
import me.minseok.springdocs.global.exception.BadRequestException;
import me.minseok.springdocs.global.exception.ErrorCode;
import me.minseok.springdocs.member.domain.Member;
import me.minseok.springdocs.member.domain.repository.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.withContainingClass(Long.class)
                .hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (nativeRequest == null) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        // TODO JWT 토큰 검증
        long memberId = Long.parseLong(nativeRequest.getParameter("memberId"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_MEMBER_ID));

        return AuthMember.member(member.getId());
    }
}
