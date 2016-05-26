/**
 *    Copyright  2014-2016 whatlookingfor@gmail.com(Jonathan)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.whatlookingfor.sample.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * swagger的实体类demo
 * 
 * @author Jonathan
 * @version 2016/5/26 14:40
 * @since JDK 7.0+
 */
@ApiModel
public class City implements Serializable{

    private static final long serialVersionUID = 1910329114849468781L;

    @ApiModelProperty(position = 1,required = true,value = "id变量",dataType = "int")
    private int id;

    @ApiModelProperty(position = 2,required = true,value = "name变量",dataType = "String")
    private String name;


    public City() {
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
