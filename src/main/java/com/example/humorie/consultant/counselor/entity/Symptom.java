package com.example.humorie.consultant.counselor.entity;

public enum Symptom {

    우울("depression", "우울"),
    스트레스("stress", "스트레스"),
    불안("anxiety", "불안"),
    조헌병("schizophrenia", "조헌병"),
    신체화("somatization", "신체화"),
    충동("impulse", "충동"),
    자존감("self_esteem", "자존감"),
    화병("hwabyung", "화병"),
    자살("suicide", "자살");

    private final String englishName;
    private final String koreanName;

    Symptom(String englishName, String koreanName) {
        this.englishName = englishName;
        this.koreanName = koreanName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public static Symptom fromKoreanName(String koreanName) {
        for (Symptom symptom : Symptom.values()) {
            if (symptom.getKoreanName().equalsIgnoreCase(koreanName)) {
                return symptom;
            }
        }
        throw new IllegalArgumentException("No matching Symptom for Korean name: " + koreanName);
    }

}
