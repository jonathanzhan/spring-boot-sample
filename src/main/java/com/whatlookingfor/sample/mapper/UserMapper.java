/*
 * Copyright  2014-2016 whatlookingfor@gmail.com(Jonathan)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whatlookingfor.sample.mapper;

import com.whatlookingfor.sample.config.mybatis.MyBatisDao;
import com.whatlookingfor.sample.model.User;

/**
 * TODO
 *
 * @author Jonathan
 * @version 2016/6/1 15:09
 * @since JDK 7.0+
 */
@MyBatisDao
public interface UserMapper {


	User getByLoginName(User user);
}
