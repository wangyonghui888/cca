package com.panda.multiterminalinteractivecenter.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainResDTO {

    /**
     *  PCDomain,多个|分割
     */
    @ApiModelProperty(value = "PCDomain")
    private String pcDomain;

    /**
     *  H5Domain,多个|分割
     */
    @NotBlank
    @ApiModelProperty(value = "H5Domain")
    private String h5Domain;

    /**
     *  ApiDomain,多个|分割
     */
    @ApiModelProperty(value = "ApiDomain")
    private String apiDomain;

    /**
     * OSSDomain,多个|分割
     */
    @ApiModelProperty(value = "OSSDomain")
    private String ossDomain;

    /**
     * OSSDomain,多个|分割
     */
    @ApiModelProperty(value = "OSSDomain")
    private String otherDomain;

    public String getDomainByType(int domainType){
        if(domainType == 1){
            return h5Domain;
        }
        if(domainType == 2){
            return pcDomain;
        }
        if(domainType == 3){
            return apiDomain;
        }
        if(domainType == 4){
            return ossDomain;
        }
        if(domainType == 5){
            return otherDomain;
        }
        return "";
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomainDetails{
        private String domain;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DomainChangedDetails{
        private String domain;
        private Boolean changed;
    }

}
