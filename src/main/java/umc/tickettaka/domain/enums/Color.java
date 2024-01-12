package umc.tickettaka.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Color {
    //todo 디자이너님께 색상 리스트 받아서 추가
    
    //예시
    BLACK("#000000");

    private final String hex;

    public String getName() {
        return name();
    }
    public String getHex() {
        return hex;
    }
}
