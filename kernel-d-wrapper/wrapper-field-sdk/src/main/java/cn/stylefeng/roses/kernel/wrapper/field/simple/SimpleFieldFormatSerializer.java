package cn.stylefeng.roses.kernel.wrapper.field.simple;

import cn.stylefeng.roses.kernel.rule.base.SimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
import cn.stylefeng.roses.kernel.wrapper.field.util.CommonFormatUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

/**
 * 针对@SimpleFieldFormat注解的具体序列化过程
 *
 * @author fengshuonan
 * @date 2022/9/6 14:09
 */
@Slf4j
public class SimpleFieldFormatSerializer extends JsonSerializer<Object> {

    /**
     * 序列化类型，覆盖还是wrapper模式
     */
    private final FormatTypeEnum formatTypeEnum;

    /**
     * 具体序列化过程
     */
    private final Class<? extends SimpleFieldFormatProcess> processClass;

    public SimpleFieldFormatSerializer(FormatTypeEnum formatTypeEnum, Class<? extends SimpleFieldFormatProcess> processClass) {
        this.formatTypeEnum = formatTypeEnum;
        this.processClass = processClass;
    }

    @Override
    public void serialize(Object originValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {

        // 创建具体字段转化的实现类
        SimpleFieldFormatProcess simpleFieldFormatProcess = null;
        try {
            simpleFieldFormatProcess = processClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("执行json的字段序列化出错", e);
            return;
        }

        // 判断当前字段值是否可以转化
        boolean canFormat = simpleFieldFormatProcess.canFormat(originValue);
        if (!canFormat) {
            return;
        }

        // 执行转化，获取转化过的值
        Object formattedValue = simpleFieldFormatProcess.formatProcess(originValue);

        // 将转化的值，根据策略，进行写入到渲染的json中
        CommonFormatUtil.writeField(formatTypeEnum, originValue, formattedValue, jsonGenerator);

    }

}
