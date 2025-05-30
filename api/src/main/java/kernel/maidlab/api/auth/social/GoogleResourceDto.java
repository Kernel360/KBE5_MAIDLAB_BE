package kernel.maidlab.api.auth.social;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleResourceDto {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

}
