package com.harmony.card;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CardRequestUserDto {
    private List<String> cardUserNames = new ArrayList<>();
}
