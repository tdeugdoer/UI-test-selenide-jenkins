package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FailMessages {
    public final String STRING_SHOULD_NOT_BE_NULL = "Строка не должна быть null";
    public final String STRING_NOT_MATCH_EXPECTED = "Полученная строка не соответствует ожидаемой";
    public final String STRING_NOT_HAS_EXPECTED = "Полученная строка не содержит ожидаемую строку";
    public final String EXPECTED_STRING_MISSING = "Отсутствует ожидаемая строка";
    public final String NUMBER_NOT_MATCH_EXPECTED = "Полученное число не соответствует ожидаемому";
    public final String NEW_WINDOW_NOT_MATCH_EXPECTED = "Новая вкладка не соответствует ожидаемой";
    public final String ELEMENT_COUNT_NOT_MATCH_EXPECTED = "Количество элементов не соответствует ожидаемому";
    public final String WINDOW_COUNT_NOT_MATCH_EXPECTED = "Количество вкладок не соответствует ожидаемому";
    public final String MENU_NOT_SORTED_BY_PRICE_ASC = "Меню не отсортировано по увеличению цены";
    public final String MENU_NOT_FILTERED_BY_MIN_PRICE = "Меню не отфильтровано по минимальной цене";
    public final String MENU_NOT_FILTERED_BY_MAX_PRICE = "Меню не отфильтровано по максимальной цене";
    public final String SLIDE_NOT_CHANGE_ELEMENTS = "Слайдер не меняет элементы";
    public final String ELEMENT_NOT_VISIBLE = "Элемент не видимый";
    public final String ELEMENT_NOT_EXIST = "Элемент не существует";
    public final String PRICE_SHOULD_INCREASE = "Цена должна увеличиться";
    public final String PRICE_SHOULD_DECREASE = "Цена должна уменьшиться";

}
