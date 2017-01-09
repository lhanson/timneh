package io.github.lhanson.timneh

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.BasicAuth
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import java.sql.Timestamp

@EnableSwagger2
@Configuration
class SwaggerConfig {
	@Bean
	Docket timnehApi() {
		new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(RequestHandlerSelectors.basePackage('io.github.lhanson'))
					.build()
				.apiInfo(apiInfo())
				.directModelSubstitute(Timestamp, String)
				.securitySchemes([new BasicAuth('basic')])
	}

	private ApiInfo apiInfo() {
		new ApiInfo(
				'Timneh',
				'Timneh API',
				'0.1',
				null, // TOS
				new Contact('Lyle Hanson', 'https://github.com/lhanson/timneh', 'timneh@lyle.33mail.com'),
				'Apache 2.0',
				'http://www.apache.org/licenses/LICENSE-2.0.html'
		)
	}
}
