package com.sprta.samsike.application.service;

import com.sprta.samsike.application.dto.response.ApiResponseDTO;
import com.sprta.samsike.application.dto.restaurant.RestaurantRequestDto;
import com.sprta.samsike.application.dto.restaurant.RestaurantResponseDto;
import com.sprta.samsike.domain.member.Member;
import com.sprta.samsike.domain.member.MemberRoleEnum;
import com.sprta.samsike.domain.region.SggCode;
import com.sprta.samsike.domain.restaurant.Category;
import com.sprta.samsike.domain.restaurant.Restaurant;
import com.sprta.samsike.infrastructure.persistence.jpa.MemberRepository;
import com.sprta.samsike.infrastructure.persistence.jpa.RestaurantRepository;
import com.sprta.samsike.presentation.advice.CustomException;
import com.sprta.samsike.presentation.advice.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private  final CategoryService categoryService;
    private final SggCodeService sggCodeService;
    private final MemberRepository memberRepository;

    private final RestaurantRegionService restaurantRegionService;


    @Transactional
    public void createRestaurant(RestaurantRequestDto requestDto) {
        // validation
        Member member = memberRepository.findByUsernameAndRole(requestDto.getUserName(), MemberRoleEnum.ROLE_OWNER.toString())
                .orElseThrow(()-> new CustomException(ErrorCode.REST004, "가게를 생성할 수 없는 유저입니다."));


        if(restaurantRepository.existsRestaurantBySggCode_SggCdAndName(requestDto.getSggCode(),requestDto.getName())){
            throw new CustomException(ErrorCode.REST003, "중복된 가게명 입니다.");
        }

        SggCode sggCode = sggCodeService.findBySggCd(requestDto.getSggCode());

        Category category = categoryService.findCategoryById(requestDto.getCategoryId());

        //Save
        Restaurant newRestaurant = Restaurant.builder()
                .name(requestDto.getName())
                .member(member)
                .imageUrl(requestDto.getImageUrl())
                .phone(requestDto.getPhone())
                .sggCode(sggCode)
                .address(requestDto.getAddress())
                .category(category)
                .build();

        restaurantRepository.save(newRestaurant);

        // RestaurantRegion 신규 생성
        restaurantRegionService.createByRestaurant(newRestaurant);
        System.out.println("생성");
    }


    @Transactional
    public ApiResponseDTO<?> updateRestaurant(String uuid, RestaurantRequestDto requestDto , Member member) {
        Restaurant restaurant = restaurantRepository.findById(UUID.fromString(uuid)).orElseThrow(()->
                new CustomException(ErrorCode.REST001,"일치하는 가게가 없습니다.")
        );

        if(member.getRole().equals(MemberRoleEnum.ROLE_OWNER.toString())
        && !restaurant.getMember().getUsername().equals(member.getUsername())){
            throw new  CustomException(ErrorCode.AUTH001, "가게를 수정할 수 있는 권한이 없습니다.");
        }

        Category category = null;
        if(requestDto.getCategoryId() !=null){
            category = categoryService.findCategoryById(requestDto.getCategoryId());
        }

        restaurant.update(requestDto, category );
        restaurantRepository.save(restaurant);

        RestaurantResponseDto responseDto = new RestaurantResponseDto(restaurant);

        return new ApiResponseDTO<>("success", responseDto);
    }

}
