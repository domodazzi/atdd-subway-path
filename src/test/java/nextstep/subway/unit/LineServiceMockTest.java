package nextstep.subway.unit;

import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.LineService;
import nextstep.subway.section.Section;
import nextstep.subway.section.SectionRequest;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationResponse;
import nextstep.subway.station.StationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private LineService lineService;

    @Test
    void addSection() {
        // given
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");

        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(선릉역.getId())).thenReturn(Optional.of(선릉역));


        // when
        SectionRequest sectionRequest = new SectionRequest(강남역.getId(), 선릉역.getId(), 7);
        lineService.addSection(1L, sectionRequest);

        // then
        assertThat(lineService.findLineById(이호선.getId()).getStations()).hasSize(2);
    }

    @Test
    void deleteSection() {
        // given
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Station 삼성역 = new Station(3L, "삼성역");

        when(lineRepository.findById(이호선.getId())).thenReturn(Optional.of(이호선));
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(선릉역.getId())).thenReturn(Optional.of(선릉역));
        when(stationRepository.findById(삼성역.getId())).thenReturn(Optional.of(삼성역));

        lineService.addSection(이호선.getId(), new SectionRequest(강남역.getId(), 선릉역.getId(), 7));
        lineService.addSection(이호선.getId(), new SectionRequest(선릉역.getId(), 삼성역.getId(), 3));

        //when
        lineService.deleteSection(이호선.getId(), 삼성역.getId());

        //then
        assertThat(lineService.findLineById(이호선.getId()).getStations()).hasSize(2);
        assertThat(lineService.findLineById(이호선.getId()).getStations()).containsAll(List.of(new StationResponse(강남역), new StationResponse(선릉역)));
    }
}
