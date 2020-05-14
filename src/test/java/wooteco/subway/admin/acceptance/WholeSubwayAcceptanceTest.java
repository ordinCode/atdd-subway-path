package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.dto.line.LineDetailResponse;
import wooteco.subway.admin.dto.line.LineResponse;
import wooteco.subway.admin.dto.station.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        //given
        LineResponse lineResponse1 = createLine("2호선");
        StationResponse stationResponse1 = createStation("강남역");
        StationResponse stationResponse2 = createStation("역삼역");
        StationResponse stationResponse3 = createStation("삼성역");
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineResponse lineResponse2 = createLine("신분당선");
        StationResponse stationResponse5 = createStation("양재역");
        StationResponse stationResponse6 = createStation("양재시민의숲역");
        addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse5.getId());
        addLineStation(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId());

        // when
        List<LineDetailResponse> response = retrieveWholeSubway().getLineDetailResponse();

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getStations().size()).isEqualTo(3);
        assertThat(response.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse retrieveWholeSubway() {
        return given().when().
            get("/lines/detail").
            then().
            statusCode(HttpStatus.OK.value()).
            extract().
            as(WholeSubwayResponse.class);
    }
}
