package umc.tickettaka.domain.enums;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum Color {
    //todo 디자이너님께 색상 리스트 받아서 추가

    //파스텔
    PASTEL_1("FFD7D7"),
    PASTEL_2("FFD1B8"),
    PASTEL_3("FFF7AF"),
    PASTEL_4("CFFFA9"),
    PASTEL_5("B4FFC9"),
    PASTEL_6("B0FFFA"),
    PASTEL_7("BDE3FF"),
    PASTEL_8("C7C6FF"),
    PASTEL_9("E4C1FF"),
    PASTEL_10("FFC6FD"),

    //비비드
    VIVID_1("FF7373"),
    VIVID_2("FFA574"),
    VIVID_3("FFEE53"),
    VIVID_4("96FF43"),
    VIVID_5("44FF78"),
    VIVID_6("54FFF3"),
    VIVID_7("51B5FF"),
    VIVID_8("6361FF"),
    VIVID_9("B455FF"),
    VIVID_10("FF5AF9"),

    //뉴트럴
    NEUTRAL_1("CFAFAF"),
    NEUTRAL_2("9F8C81"),
    NEUTRAL_3("CBBCA5"),
    NEUTRAL_4("A7BC9A"),
    NEUTRAL_5("7D9E8A"),
    NEUTRAL_6("49A0A0"),
    NEUTRAL_7("9EB1D4"),
    NEUTRAL_8("7875B4"),
    NEUTRAL_9("9C83A8"),
    NEUTRAL_10("000000");


    private final String hex;

    public String getName() {
        return name();
    }
    public String getHex() {
        return hex;
    }

    public static Color getRandomColor() {
        List<Color> colors = Arrays.asList(values());
        Collections.shuffle(colors);
        return colors.get(0);
    }
}
