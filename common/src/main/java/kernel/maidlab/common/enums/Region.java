package kernel.maidlab.common.enums;

import lombok.Getter;

@Getter
public enum Region {
	GANGNAM("1"),
	GANGDONG("2"),
	GANGBUK("3"),
	GANGSEO("4"),
	GWANAK("5"),
	GWANGJIN("6"),
	GURO("7"),
	GEUMCHEON("8"),
	NOWON("9"),
	DOBONG("10"),
	DONGDAEMUN("11"),
	DONGJAK("12"),
	MAPO("13"),
	SEODAEMUN("14"),
	SEOCHO("15"),
	SEONGDONG("16"),
	SEONGBUK("17"),
	SONGPA("18"),
	YANGCHEON("19"),
	YEONGDEUNGPO("20"),
	YONGSAN("21"),
	EUNPYEONG("22"),
	JONGNO("23"),
	JUNG("24"),
	JUNGNANG("25");

	private final String name;

	Region(String name) {
		this.name = name;
	}

}
