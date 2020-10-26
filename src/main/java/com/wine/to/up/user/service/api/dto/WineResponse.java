package com.wine.to.up.user.service.api;

import com.wine.to.up.user.service.api.dto.UserTokens;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WineResponse {
    private Long wineId;
    private String wineName;
    private Float newWinePrice;
    private List<UserTokens> userTokens;
}
