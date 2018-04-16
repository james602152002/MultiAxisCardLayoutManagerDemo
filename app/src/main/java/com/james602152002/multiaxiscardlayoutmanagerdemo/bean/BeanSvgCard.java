package com.james602152002.multiaxiscardlayoutmanagerdemo.bean;

public class BeanSvgCard {
    private int svg_str_arr_id;
    private int svg_color_arr_id;

    public BeanSvgCard(int svg_str_arr_id, int svg_color_arr_id) {
        this.svg_str_arr_id = svg_str_arr_id;
        this.svg_color_arr_id = svg_color_arr_id;
    }

    public int getSvg_str_arr_id() {
        return svg_str_arr_id;
    }

    public void setSvg_str_arr_id(int svg_str_arr_id) {
        this.svg_str_arr_id = svg_str_arr_id;
    }

    public int getSvg_color_arr_id() {
        return svg_color_arr_id;
    }

    public void setSvg_color_arr_id(int svg_color_arr_id) {
        this.svg_color_arr_id = svg_color_arr_id;
    }
}
