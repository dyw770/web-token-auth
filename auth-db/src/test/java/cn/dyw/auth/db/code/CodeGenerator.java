package cn.dyw.auth.db.code;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.query.SQLQuery;
import org.apache.ibatis.annotations.Mapper;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * @author dyw770
 * @since 2025-04-18
 */
public class CodeGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.19.15:3306/spring_security_auth?serverTimezone=GMT%2B8",
                        "root", "P@ssw0rd2022")
                .dataSourceConfig(
                        builder ->
                                builder.databaseQueryClass(SQLQuery.class)
                                        .typeConvert(new MySqlTypeConvert())
                                        .dbQuery(new MySqlQuery())
                                        .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                                            if (typeCode == Types.TINYINT) {
                                                // 自定义类型转换
                                                return DbColumnType.BASE_BOOLEAN;
                                            }
                                            return typeRegistry.getColumnType(metaInfo);
                                        })
                )
                .globalConfig(builder -> builder
                        .disableOpenDir()
                        .author("dyw770")
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                )
                .packageConfig(builder -> builder
                        .parent("cn.dyw.auth.db")
                        .entity("domain")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mappers")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, Paths.get(System.getProperty("user.dir")) + "/src/main/resources/mappers"))
                )
                .strategyConfig(builder -> {
                            builder
                                    .entityBuilder()
                                    .disableSerialVersionUID()
                                    .enableLombok()
                                    .controllerBuilder().disable()
                                    .mapperBuilder()
                                    .mapperAnnotation(Mapper.class);
                        }
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
