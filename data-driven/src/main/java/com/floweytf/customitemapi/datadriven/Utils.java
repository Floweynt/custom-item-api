package com.floweytf.customitemapi.datadriven;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.TreeMap;

public class Utils {
    private static final DecimalFormat DF = new DecimalFormat("#.####");
    private final static TreeMap<Integer, String> ROMAN_NUMERAL_MAP = new TreeMap<>();

    static {
        ROMAN_NUMERAL_MAP.put(1000, "M");
        ROMAN_NUMERAL_MAP.put(900, "CM");
        ROMAN_NUMERAL_MAP.put(500, "D");
        ROMAN_NUMERAL_MAP.put(400, "CD");
        ROMAN_NUMERAL_MAP.put(100, "C");
        ROMAN_NUMERAL_MAP.put(90, "XC");
        ROMAN_NUMERAL_MAP.put(50, "L");
        ROMAN_NUMERAL_MAP.put(40, "XL");
        ROMAN_NUMERAL_MAP.put(10, "X");
        ROMAN_NUMERAL_MAP.put(9, "IX");
        ROMAN_NUMERAL_MAP.put(5, "V");
        ROMAN_NUMERAL_MAP.put(4, "IV");
        ROMAN_NUMERAL_MAP.put(1, "I");
    }

    public static String fmtFloat(double x, boolean plus) {
        return (plus && x > 0 ? "+" : "") + DF.format(x);
    }

    public static String fmtFloat(double x) {
        return (x > 0 ? "+" : "") + DF.format(x);
    }

    public static String toRoman(int number) {
        int l = ROMAN_NUMERAL_MAP.floorKey(number);
        if (number == l) {
            return ROMAN_NUMERAL_MAP.get(number);
        }
        return ROMAN_NUMERAL_MAP.get(l) + toRoman(number - l);
    }

    public static TextColor colorFromString(String color) {
        return color.startsWith("#") ? TextColor.fromHexString(color) : NamedTextColor.NAMES.value(color);
    }

    public static Component getComponent(JsonObject object, String key) {
        return GsonComponentSerializer.gson().deserializeFromTree(object.get(key));
    }

    public static Optional<String> tryGetString(JsonObject object, String key) {
        return object.has(key) ? Optional.of(object.get(key).getAsString()) : Optional.empty();
    }

    public static Optional<JsonElement> tryGetJsonElement(JsonObject object, String key) {
        return object.has(key) ? Optional.of(object.get(key)) : Optional.empty();
    }
}