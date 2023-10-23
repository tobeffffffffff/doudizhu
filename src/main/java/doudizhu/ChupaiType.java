

package doudizhu;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum ChupaiType {

    DUI_ZI("对子"),
    SHUN_ZI("顺子"),
    DAN_ZHANG("单张"),
    SAN_ZHANG("三带N"),
    LIAN_DUI("连对"),
    FEI_JI("飞机"),
    ZHA_DAN("炸弹"),
    WANG_ZHA("王炸");

    @Getter
    private String name;

}
