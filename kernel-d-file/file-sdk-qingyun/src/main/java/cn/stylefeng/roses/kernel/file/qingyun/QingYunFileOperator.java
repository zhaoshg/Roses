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
package cn.stylefeng.roses.kernel.file.qingyun;

import cn.stylefeng.roses.kernel.file.api.FileOperatorApi;
import cn.stylefeng.roses.kernel.file.api.enums.BucketAuthEnum;
import cn.stylefeng.roses.kernel.file.api.enums.FileLocationEnum;
import cn.stylefeng.roses.kernel.file.api.pojo.props.QingYunOssProperties;

import java.io.InputStream;

/**
 * 青云的OSS操作
 *
 * @author fengshuonan
 * @since 2023/11/26 22:01
 */
public class QingYunFileOperator implements FileOperatorApi {

    private final QingYunOssProperties qingYunOssProperties;

    public QingYunFileOperator(QingYunOssProperties qingYunOssProperties) {
        this.qingYunOssProperties = qingYunOssProperties;
        this.initClient();
    }

    @Override
    public void initClient() {
    }

    @Override
    public void destroyClient() {

    }

    @Override
    public Object getClient() {
        return null;
    }

    @Override
    public boolean doesBucketExist(String bucketName) {
        return false;
    }

    @Override
    public void setBucketAcl(String bucketName, BucketAuthEnum bucketAuthEnum) {

    }

    @Override
    public boolean isExistingFile(String bucketName, String key) {
        return false;
    }

    @Override
    public void storageFile(String bucketName, String key, byte[] bytes) {

    }

    @Override
    public void storageFile(String bucketName, String key, InputStream inputStream) {

    }

    @Override
    public byte[] getFileBytes(String bucketName, String key) {
        return new byte[0];
    }

    @Override
    public void setFileAcl(String bucketName, String key, BucketAuthEnum bucketAuthEnum) {

    }

    @Override
    public void copyFile(String originBucketName, String originFileKey, String newBucketName, String newFileKey) {

    }

    @Override
    public String getFileAuthUrl(String bucketName, String key, Long timeoutMillis) {
        return null;
    }

    @Override
    public String getFileUnAuthUrl(String bucketName, String key) {
        return null;
    }

    @Override
    public void deleteFile(String bucketName, String key) {

    }

    @Override
    public FileLocationEnum getFileLocationEnum() {
        return null;
    }

}
