/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
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
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.sys.modular.role.pojo.response;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.util.List;

/**
 * 角色绑定权限界面的响应封装
 * <p>
 * 本结构是个树形结构，第1层级是应用，第2层级是应用下的菜单（菜单只显示最子节点），第3层级是菜单下的功能options
 *
 * @author fengshuonan
 * @since 2023/6/13 13:54
 */
@Data
public class RoleBindPermissionResponse implements AbstractTreeNode<RoleBindPermissionResponse> {

    /**
     * 节点ID，可以是菜单id和按钮id
     */
    @ChineseDescription("节点ID")
    private Long nodeId;

    /**
     * 节点父ID
     */
    @ChineseDescription("节点父ID")
    private Long nodeParentId;

    /**
     * 节点名称
     */
    @ChineseDescription("节点名称")
    private String nodeName;

    /**
     * 节点编码
     */
    @ChineseDescription("节点编码")
    private String nodeCode;

    /**
     * 是否选择(已拥有的是true)
     */
    @ChineseDescription("是否选择(已拥有的是true)")
    private Boolean checked;

    /**
     * 子节点集合
     */
    @ChineseDescription("子节点集合")
    private List<RoleBindPermissionResponse> children;

    @Override
    public String getNodeId() {
        if (this.nodeId != null) {
            return this.nodeId.toString();
        } else {
            return "";
        }
    }

    @Override
    public String getNodeParentId() {
        if (this.nodeParentId != null) {
            return this.nodeParentId.toString();
        } else {
            return "";
        }
    }

    @Override
    public void setChildrenNodes(List<RoleBindPermissionResponse> childrenNodes) {
        this.children = childrenNodes;
    }

}
