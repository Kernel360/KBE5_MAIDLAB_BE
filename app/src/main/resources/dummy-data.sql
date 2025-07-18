-- ==============================
-- 대용량 더미 데이터 스크립트
-- ==============================
--
-- 비밀번호:
-- - 관리자: "admin1234", "maidlab1234" (BCrypt 암호화)
-- - 일반 사용자: "maidlab1234" (BCrypt 암호화)
-- - 소셜 로그인: 비밀번호 없음 (NULL)

-- ==============================
-- Admin 더미 데이터
-- ==============================

INSERT INTO admin (admin_key, password, refresh_token, is_deleted, created_at, updated_at)
VALUES ('admin', '$2a$10$3nI3fndjrryAtu6hW9LEEuxf1wWpH47d.SI3wAiDCAoKLTIXFDIYO', NULL, false, NOW(), NOW()),
       ('ssony', '$2a$10$bGhrGZy2iPv/TUtKiKigQ.eQRG6ouJUUwMBPn2.VmGlPFExYtb3CC', NULL, false, NOW(), NOW()),
       ('jinsung', '$2a$10$bGhrGZy2iPv/TUtKiKigQ.eQRG6ouJUUwMBPn2.VmGlPFExYtb3CC', NULL, false, NOW(), NOW()),
       ('seunghyun', '$2a$10$bGhrGZy2iPv/TUtKiKigQ.eQRG6ouJUUwMBPn2.VmGlPFExYtb3CC', NULL, false, NOW(), NOW()),
       ('jaeho', '$2a$10$bGhrGZy2iPv/TUtKiKigQ.eQRG6ouJUUwMBPn2.VmGlPFExYtb3CC', NULL, false, NOW(), NOW());

-- ==============================
-- Consumer 대용량 더미 데이터
-- ==============================

-- 일반 회원
INSERT INTO consumer (phone_number, password, name, gender, birth, profile_image, point, average_rate,
                      total_reviewed_cnt, address, detail_address, uuid, social_type, emergency_call, is_deleted,
                      created_at, updated_at)
SELECT CONCAT('010', LPAD(FLOOR(RAND() * 100000000), 8, '0'))         as phone_number,
       '$2a$10$bGhrGZy2iPv/TUtKiKigQ.eQRG6ouJUUwMBPn2.VmGlPFExYtb3CC' as password,
       CONCAT(
               ELT(FLOOR(RAND() * 20) + 1, '김', '이', '박', '최', '정', '강', '조', '윤', '장', '임', '한', '오', '서', '신', '권',
                   '황', '안', '송', '전', '홍'),
               ELT(FLOOR(RAND() * 20) + 1, '민수', '지영', '성훈', '서연', '대호', '수진', '준호', '하늘', '동현', '은정', '민철', '유리', '길동',
                   '수연', '태현', '진주', '현우', '소영', '병수', '예진')
       )                                                              as name,
       ELT(FLOOR(RAND() * 2) + 1, 'MALE', 'FEMALE')                   as gender,
       DATE_ADD('1980-01-01', INTERVAL FLOOR(RAND() * 15000) DAY)     as birth,
       CASE
           WHEN RAND() > 0.3
               THEN 'https://d1llec2m3tvk5i.cloudfront.net/uploads/645f06ba-38e1-4f2a-9bf1-0a5ed4f16bee_뻬짱.png'
           ELSE NULL END                                              as profile_image,
       FLOOR(RAND() * 10000)                                          as point,
       ROUND(3.0 + RAND() * 2.0, 1)                                   as average_rate,
       FLOOR(RAND() * 50)                                             as total_reviewed_cnt,
       CONCAT('서울시 ',
              ELT(FLOOR(RAND() * 25) + 1, '강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구',
                  '동작구', '마포구', '서대문구', '서초구', '성동구', '성북구', '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구'),
              ' ',
              ELT(FLOOR(RAND() * 10) + 1, '역삼동', '논현동', '압구정동', '청담동', '삼성동', '대치동', '개포동', '신사동', '강남동', '도곡동'),
              ' ',
              FLOOR(RAND() * 999) + 1
       )                                                              as address,
       CONCAT(
               ELT(FLOOR(RAND() * 5) + 1, '아파트', '오피스텔', '빌라', '원룸', '다세대주택'),
               ' ',
               CHAR(65 + FLOOR(RAND() * 5)),
               '동 ',
               FLOOR(RAND() * 20) + 1,
               LPAD(FLOOR(RAND() * 99) + 1, 2, '0'),
               '호'
       )                                                              as detail_address,
       UUID()                                                         as uuid,
       NULL                                                           as social_type,
       NULL                                                           as emergency_call,
       false                                                          as is_deleted,
       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)              as created_at,
       NOW()                                                          as updated_at
FROM (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t1,
     (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t2,
     (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8) t3
LIMIT 680;

-- 소셜 로그인 회원
INSERT INTO consumer (phone_number, password, name, gender, birth, profile_image, point, average_rate,
                      total_reviewed_cnt, address, detail_address, uuid, social_type, emergency_call, is_deleted,
                      created_at, updated_at)
SELECT CONCAT('google-', SUBSTRING(MD5(RAND()), 1, 8))                       as phone_number,
       NULL                                                                  as password,
       CONCAT(
               ELT(FLOOR(RAND() * 20) + 1, '김', '이', '박', '최', '정', '강', '조', '윤', '장', '임', '한', '오', '서', '신', '권',
                   '황', '안', '송', '전', '홍'),
               ELT(FLOOR(RAND() * 20) + 1, '민수', '지영', '성훈', '서연', '대호', '수진', '준호', '하늘', '동현', '은정', '민철', '유리', '길동',
                   '수연', '태현', '진주', '현우', '소영', '병수', '예진')
       )                                                                     as name,
       ELT(FLOOR(RAND() * 2) + 1, 'MALE', 'FEMALE')                          as gender,
       DATE_ADD('1980-01-01', INTERVAL FLOOR(RAND() * 15000) DAY)            as birth,
       CASE
           WHEN RAND() > 0.7
               THEN 'https://d1llec2m3tvk5i.cloudfront.net/uploads/645f06ba-38e1-4f2a-9bf1-0a5ed4f16bee_뻬짱.png'
           ELSE NULL END                                                     as profile_image,
       FLOOR(RAND() * 5000)                                                  as point,
       CASE WHEN RAND() > 0.8 THEN ROUND(3.0 + RAND() * 2.0, 1) ELSE 0.0 END as average_rate,
       CASE WHEN RAND() > 0.8 THEN FLOOR(RAND() * 20) ELSE 0 END             as total_reviewed_cnt,
       CONCAT('서울시 ',
              ELT(FLOOR(RAND() * 25) + 1, '강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구',
                  '동작구', '마포구', '서대문구', '서초구', '성동구', '성북구', '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구'),
              ' ',
              ELT(FLOOR(RAND() * 10) + 1, '역삼동', '논현동', '압구정동', '청담동', '삼성동', '대치동', '개포동', '신사동', '강남동', '도곡동'),
              ' ',
              FLOOR(RAND() * 999) + 1
       )                                                                     as address,
       CONCAT(
               ELT(FLOOR(RAND() * 5) + 1, '아파트', '오피스텔', '빌라', '원룸', '다세대주택'),
               ' ',
               CHAR(65 + FLOOR(RAND() * 5)),
               '동 ',
               FLOOR(RAND() * 20) + 1,
               LPAD(FLOOR(RAND() * 99) + 1, 2, '0'),
               '호'
       )                                                                     as detail_address,
       UUID()                                                                as uuid,
       'GOOGLE'                                                              as social_type, -- 원본 데이터와 동일한 값 사용
       CASE
           WHEN RAND() > 0.7 THEN CONCAT('010-', LPAD(FLOOR(RAND() * 10000), 4, '0'), '-',
                                         LPAD(FLOOR(RAND() * 10000), 4, '0'))
           ELSE NULL END                                                     as emergency_call,
       false                                                                 as is_deleted,
       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)                     as created_at,
       NOW()                                                                 as updated_at
FROM (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t1,
     (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t2,
     (SELECT 1 as n UNION SELECT 2 UNION SELECT 3) t3
LIMIT 300;

-- ==============================
-- Manager 대용량 더미 데이터
-- ==============================

-- 기본 매니저들
INSERT INTO manager (phone_number, password, name, gender, birth, profile_image, introduce_text, average_rate,
                     total_reviewed_cnt, bank, is_verified, uuid, social_type, emergency_call, is_deleted, created_at,
                     updated_at)
SELECT CONCAT('010', LPAD(FLOOR(RAND() * 100000000), 8, '0'))                                    as phone_number,
       '$2a$10$bGhrGZy2iPv/TUtKiKigQ.eQRG6ouJUUwMBPn2.VmGlPFExYtb3CC'                            as password,
       CONCAT(
               ELT(FLOOR(RAND() * 20) + 1, '김', '이', '박', '최', '정', '강', '조', '윤', '장', '임', '한', '오', '서', '신', '권',
                   '황', '안', '송', '전', '홍'),
               ELT(FLOOR(RAND() * 30) + 1, '수민', '지현', '민준', '서윤', '예준', '시은', '도윤', '채원', '건우', '지우', '우진', '연우', '하준',
                   '서연', '주원', '서현', '지호', '유나', '현우', '가은', '시우', '윤서', '준우', '다은', '지안', '소윤', '현준', '정우', '수아', '서준')
       )                                                                                         as name,
       ELT(FLOOR(RAND() * 2) + 1, 'MALE', 'FEMALE')                                              as gender,
       DATE_ADD('1975-01-01', INTERVAL FLOOR(RAND() * 18000) DAY)                                as birth,
       CASE
           WHEN RAND() > 0.2
               THEN 'https://d1llec2m3tvk5i.cloudfront.net/uploads/6c4322df-fa97-4b76-ab8e-c145778abbcc_스누피 개미.png'
           ELSE NULL END                                                                         as profile_image,
       CONCAT(
               FLOOR(RAND() * 15) + 1, '년 경력의 ',
               ELT(FLOOR(RAND() * 6) + 1, '전문 청소', '아이 돌봄', '반려동물 케어', '가사 도우미', '시니어 케어', '특수 청소'),
               ' 전문가입니다. ',
               ELT(FLOOR(RAND() * 10) + 1, '꼼꼼하고 성실한', '친절하고 정성스러운', '전문적이고 체계적인', '안전하고 믿을 수 있는', '경험이 풍부한',
                   '고객 만족을 최우선으로 하는', '깨끗하고 효율적인', '세심하고 배려심 깊은', '책임감 있고 신뢰할 수 있는', '정확하고 빠른'),
               ' 서비스를 제공합니다.'
       )                                                                                         as introduce_text,
       ROUND(3.5 + RAND() * 1.5, 1)                                                              as average_rate,
       FLOOR(RAND() * 100)                                                                       as total_reviewed_cnt,
       ELT(FLOOR(RAND() * 8) + 1, '국민은행', '신한은행', '하나은행', '우리은행', '기업은행', '농협', '카카오뱅크', '토스뱅크') as bank,
       FLOOR(RAND() * 2)                                                                         as is_verified,
       UUID()                                                                                    as uuid,
       NULL                                                                                      as social_type,
       NULL                                                                                      as emergency_call,
       false                                                                                     as is_deleted,
       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 730) DAY)                                         as created_at,
       NOW()                                                                                     as updated_at
FROM (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t1,
     (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t2,
     (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) t3
LIMIT 495;

-- ==============================
-- Manager Region 연결 데이터 (대용량)
-- ==============================
INSERT INTO manager_region (manager_id, region_id)
SELECT m.id                   as manager_id,
       FLOOR(RAND() * 25) + 1 as region_id
FROM manager m
         CROSS JOIN (SELECT 1 as n UNION SELECT 2 UNION SELECT 3) t
WHERE RAND() > 0.3 -- 약 70%의 매니저들이 평균 2-3개 지역에서 활동
ORDER BY RAND()
LIMIT 1000;

-- ==============================
-- Reservation 대용량 더미 데이터 (5000건)
-- ==============================
INSERT INTO reservation (manager_id, consumer_id, service_detail_type_id, reservation_date, start_time, end_time,
                         address, address_detail, housing_type, room_size, housing_information, service_add, pet,
                         special_request, total_price, status, canceled_at, checkin_time, checkout_time,
                         final_payment_price, created_at, updated_at)
SELECT FLOOR(RAND() * 500) + 1                                        as manager_id,             -- 매니저 ID (1-500)
       FLOOR(RAND() * 1000) + 1                                       as consumer_id,            -- 소비자 ID (1-1000)
       FLOOR(RAND() * 6) + 1                                          as service_detail_type_id, -- 서비스 타입 (1-6)
       DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 450) DAY)       as reservation_date,
       DATE_ADD(DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 450) DAY), INTERVAL FLOOR(RAND() * 14 + 6)
                HOUR)                                                 as start_time,
       DATE_ADD(
               DATE_ADD(DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 450) DAY), INTERVAL FLOOR(RAND() * 14 + 6) HOUR),
               INTERVAL FLOOR(RAND() * 8 + 2)
               HOUR)                                                  as end_time,
       CONCAT('서울시 ',
              ELT(FLOOR(RAND() * 25) + 1, '강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구',
                  '동작구', '마포구', '서대문구', '서초구', '성동구', '성북구', '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구'),
              ' ',
              ELT(FLOOR(RAND() * 10) + 1, '역삼동', '논현동', '압구정동', '청담동', '삼성동', '대치동', '개포동', '신사동', '강남동', '도곡동'),
              ' ',
              FLOOR(RAND() * 999) + 1
       )                                                              as address,
       CONCAT(
               ELT(FLOOR(RAND() * 5) + 1, '아파트', '오피스텔', '빌라', '원룸', '다세대주택'),
               ' ',
               CHAR(65 + FLOOR(RAND() * 5)),
               '동 ',
               FLOOR(RAND() * 20) + 1,
               LPAD(FLOOR(RAND() * 99) + 1, 2, '0'),
               '호'
       )                                                              as address_detail,
       ELT(FLOOR(RAND() * 5) + 1, '아파트', '오피스텔', '빌라', '원룸', '다세대주택') as housing_type,
       FLOOR(RAND() * 5) + 1                                          as room_size,
       CONCAT(
               ELT(FLOOR(RAND() * 8) + 1, '신축', '준신축', '오래된', '리모델링한', '깨끗한', '넓은', '작은', '일반적인'),
               ' ',
               ELT(FLOOR(RAND() * 5) + 1, '아파트', '오피스텔', '빌라', '원룸', '다세대주택'),
               CASE
                   WHEN RAND() > 0.7 THEN CONCAT(', ', ELT(FLOOR(RAND() * 5) + 1, '반려동물 있음', '아이 있음', '어르신 거주', '신혼부부',
                                                           '1인 거주'))
                   ELSE '' END
       )                                                              as housing_information,
       CASE
           WHEN RAND() > 0.6 THEN ELT(FLOOR(RAND() * 10) + 1, '냉장고청소', '오븐청소', '화장실청소', '베란다청소', '놀이활동', '급식보조', '학습지도',
                                      '산책', '목욕', '응급처치')
           ELSE NULL
           END                                                        as service_add,
       CASE
           WHEN RAND() > 0.8 THEN ELT(FLOOR(RAND() * 8) + 1, '골든리트리버', '말티즈', '푸들', '비숑', '치와와', '포메라니안', '요크셔테리어',
                                      '믹스견')
           ELSE NULL
           END                                                        as pet,
       CASE
           WHEN RAND() > 0.5 THEN ELT(FLOOR(RAND() * 15) + 1,
                                      '꼼꼼하게 청소해주세요.', '시간 약속 잘 지켜주세요.', '반려동물에게 친절하게 해주세요.',
                                      '아이가 낯을 가려요.', '알레르기 주의해주세요.', '조용히 해주세요.', '문 잠금 확인해주세요.',
                                      '청소용품은 제공됩니다.', '주차는 지하주차장에 해주세요.', '엘리베이터는 A동 쪽입니다.',
                                      '특별한 요청사항 없습니다.', '깨끗하게 해주세요.', '안전하게 해주세요.', '친절하게 부탁드립니다.')
           ELSE '특별한 요청사항 없습니다.'
           END                                                        as special_request,
       FLOOR(RAND() * 150000 + 30000)                                 as total_price,            -- 30,000 ~ 180,000원
       ELT(FLOOR(RAND() * 6) + 1, 'PENDING', 'MATCHED', 'PAID', 'WORKING', 'COMPLETED',
           'CANCELED')                                                as status,
       CASE
           WHEN RAND() > 0.9 THEN DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)
           ELSE NULL END                                              as canceled_at,
       CASE
           WHEN RAND() > 0.5 THEN DATE_ADD(DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 450) DAY), INTERVAL
                                           FLOOR(RAND() * 14 + 6) HOUR)
           ELSE NULL END                                              as checkin_time,
       CASE
           WHEN RAND() > 0.6 THEN DATE_ADD(
                   DATE_ADD(DATE_ADD('2025-01-01', INTERVAL FLOOR(RAND() * 450) DAY), INTERVAL FLOOR(RAND() * 14 + 6)
                            HOUR), INTERVAL FLOOR(RAND() * 8 + 2) HOUR)
           ELSE NULL END                                              as checkout_time,
       FLOOR(RAND() * 150000 + 30000)                                 as final_payment_price,
       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)              as created_at,
       NOW()                                                          as updated_at
FROM (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t1,
     (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t2,
     (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) t3
LIMIT 4995;

-- ==============================
-- Point 대용량 더미 데이터 (10000건)
-- ==============================
INSERT INTO point (consumer_id, amount, point_type, description, created_at, updated_at)
SELECT c.id                                                     as consumer_id,
       CASE
           WHEN RAND() > 0.6 THEN FLOOR(RAND() * 5000 + 100) -- 적립 포인트
           ELSE -FLOOR(RAND() * 10000 + 500) -- 사용 포인트
           END                                                  as amount,
       ELT(FLOOR(RAND() * 3) + 1, 'PAYMENT', 'CHARGE', 'EVENT') as point_type,
       ELT(FLOOR(RAND() * 10) + 1,
           '결제 적립 포인트', '결제 사용 포인트', '포인트 충전', '이벤트 적립',
           '추천인 적립', '리뷰 작성 적립', '첫 이용 적립', '생일 축하 적립',
           '등급 승격 적립', '특별 이벤트 적립'
       )                                                        as description,
       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)        as created_at,
       NOW()                                                    as updated_at
FROM consumer c
         CROSS JOIN (SELECT 1 as n
                     UNION
                     SELECT 2
                     UNION
                     SELECT 3
                     UNION
                     SELECT 4
                     UNION
                     SELECT 5
                     UNION
                     SELECT 6
                     UNION
                     SELECT 7
                     UNION
                     SELECT 8
                     UNION
                     SELECT 9
                     UNION
                     SELECT 10) multiplier
WHERE RAND() > 0.2
LIMIT 10000;

-- ==============================
-- Review 대용량 더미 데이터 (8000건)
-- ==============================
INSERT INTO review (reservation_id, manager_id, consumer_id, rating, comment, service_detail_type_id, review_date,
                    is_consumer_to_manager, created_at, updated_at)
SELECT r.id                                                   as reservation_id,
       r.manager_id,
       r.consumer_id,
       CAST(ROUND(3.0 + RAND() * 2.0, 1) AS DECIMAL(3, 1))    as rating,
       ELT(FLOOR(RAND() * 30) + 1,
           '정말 깔끔하게 청소해주셨어요. 다음에도 부탁드리고 싶습니다.',
           '시간 약속을 잘 지켜주시는 좋은 고객이었습니다.',
           '아이를 정말 잘 돌봐주셨어요. 안심하고 맡길 수 있었습니다.',
           '반려동물을 정성스럽게 돌봐주셔서 감사합니다.',
           '꼼꼼하고 성실하게 일해주셨어요.',
           '친절하고 전문적인 서비스였습니다.',
           '기대 이상으로 만족스러웠습니다.',
           '정말 깨끗하게 해주셨네요!',
           '다음에도 꼭 이용하고 싶어요.',
           '매우 만족합니다. 추천드립니다.',
           '정시에 와서 꼼꼼하게 해주셨어요.',
           '아이가 정말 좋아했어요.',
           '반려동물도 스트레스받지 않고 잘 지냈어요.',
           '청소 실력이 정말 좋으세요.',
           '신뢰할 수 있는 매니저님이에요.',
           '가격 대비 만족스러운 서비스였습니다.',
           '또 이용할 예정입니다.',
           '정말 고마웠습니다.',
           '완벽한 서비스였어요.',
           '기대했던 것보다 더 좋았습니다.',
           '매우 전문적이고 친절하셨어요.',
           '시간을 잘 지켜주셨어요.',
           '깨끗하게 정리정돈해주셨습니다.',
           '안전하게 돌봐주셔서 안심됐어요.',
           '세심한 배려가 느껴졌습니다.',
           '정말 만족스러운 서비스였습니다.',
           '다음에도 같은 매니저님께 부탁드리고 싶어요.',
           '가족 모두 만족했습니다.',
           '최고의 서비스였어요!',
           '정말 감사드립니다.'
       )                                                      as comment,
       r.service_detail_type_id,
       DATE_ADD(r.start_time, INTERVAL FLOOR(RAND() * 7) DAY) as review_date,
       RAND() > 0.5                                           as is_consumer_to_manager,
       DATE_ADD(r.created_at, INTERVAL FLOOR(RAND() * 7) DAY) as created_at,
       NOW()                                                  as updated_at
FROM reservation r
WHERE r.status IN ('COMPLETED', 'WORKING')
  AND RAND() > 0.3 -- 약 70%의 완료된 예약에 리뷰 생성
LIMIT 8000;

-- ==============================
-- Board 대용량 더미 데이터 (2000건)
-- ==============================
INSERT INTO board (consumer_id, manager_id, board_type, title, content, is_answered, is_deleted, created_at, updated_at)
SELECT CASE WHEN RAND() > 0.3 THEN (SELECT id FROM consumer ORDER BY RAND() LIMIT 1) ELSE NULL END as consumer_id,
       CASE WHEN RAND() > 0.7 THEN (SELECT id FROM manager ORDER BY RAND() LIMIT 1) ELSE NULL END  as manager_id,
       -- 일반적인 board_type 값들로 수정 (대부분의 시스템에서 사용하는 값들)
       ELT(FLOOR(RAND() * 4) + 1, 'REFUND', 'MANAGER', 'SERVICE', 'ETC')                           as board_type,
       ELT(FLOOR(RAND() * 25) + 1,
           '청소 서비스 문의', '베이비시터 서비스 관련 질문', '펫케어 서비스 이용 문의',
           '결제 관련 문의', '환불 요청', '서비스 불만 접수', '매니저 변경 요청',
           '서비스 시간 변경 문의', '정기 서비스 신청', '특별 서비스 문의',
           '보험 관련 질문', '안전 사고 신고', '서비스 품질 개선 제안',
           '앱 사용법 문의', '회원가입 문제', '로그인 오류', 'FAQ 문의',
           '프로모션 관련 질문', '포인트 사용 문의', '리뷰 작성 문의',
           '매니저 추천 요청', '서비스 지역 확대 요청', '가격 문의',
           '예약 취소 문의', '기타 문의사항'
       )                                                                                           as title,
       ELT(FLOOR(RAND() * 20) + 1,
           '생활청소 서비스를 이용하고 싶은데, 어떤 준비를 해야 하나요?',
           '베이비시터 서비스 이용 중 아이가 다쳤습니다. 어떻게 해야 하나요?',
           '청소 시 사용하는 세제에 대한 안내입니다.',
           '대형견도 돌봄 서비스가 가능한가요?',
           '정기 서비스 할인이 있나요?',
           '매니저 변경이 가능한가요?',
           '예약 취소 수수료가 있나요?',
           '서비스 시간을 연장할 수 있나요?',
           '보험 적용이 어떻게 되나요?',
           '결제 방법에는 어떤 것들이 있나요?',
           '포인트 사용 방법을 알려주세요.',
           '리뷰 작성하면 포인트가 적립되나요?',
           '서비스 지역을 확인하고 싶어요.',
           '매니저 프로필을 미리 볼 수 있나요?',
           '특별한 요청사항이 있는데 가능한가요?',
           '서비스 품질에 만족하지 못했어요.',
           '앱에서 오류가 발생해요.',
           '회원가입이 안 돼요.',
           '비밀번호를 잊어버렸어요.',
           '서비스 이용 후기가 궁금해요.'
       )                                                                                           as content,
       RAND() > 0.6                                                                                as is_answered,
       false                                                                                       as is_deleted,
       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)                                           as created_at,
       NOW()                                                                                       as updated_at
FROM (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t1,
     (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t2,
     (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t3,
     (SELECT 1 as n UNION SELECT 2) t4
LIMIT 2000;

-- ==============================
-- Board Image 더미 데이터 (500건)
-- ==============================
INSERT INTO board_image (board_id, image_path, name)
SELECT b.id as board_id,
       ELT(FLOOR(RAND() * 5) + 1,
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/b1b0c808-e854-4540-9fc7-55d4fc0f8b48_스크린샷 2025-07-17 오후 1.30.47.png',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/6c4322df-fa97-4b76-ab8e-c145778abbcc_스누피 개미.png',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/645f06ba-38e1-4f2a-9bf1-0a5ed4f16bee_뻬짱.png',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/1eab54ed-c5b2-48f4-b7c0-301d788d8350_상장.jpg',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/26a154a2-8db7-4794-bb03-ee5f121463eb_이미지 메인 1.jpg'
       )    as image_path,
       ELT(FLOOR(RAND() * 10) + 1,
           'clean.jpg', 'baby.jpg', 'pet.jpg', 'house.jpg', 'service.jpg',
           'problem.jpg', 'question.jpg', 'help.jpg', 'review.jpg', 'before_after.jpg'
       )    as name
FROM board b
WHERE RAND() > 0.75 -- 약 25%의 게시글에 이미지 추가
LIMIT 500;

-- ==============================
-- Answer 더미 데이터 (1200건)
-- ==============================
INSERT INTO answer (board_id, content, created_at, updated_at)
SELECT b.id                                                       as board_id,
       ELT(FLOOR(RAND() * 15) + 1,
           '안전사고 관련해서는 즉시 고객센터로 연락주시기 바랍니다. 보험 처리 등 필요한 조치를 도와드리겠습니다.',
           '문의해주신 내용에 대해 상세히 안내드리겠습니다. 추가 문의사항이 있으시면 언제든 연락주세요.',
           '서비스 이용에 불편을 드려 죄송합니다. 개선방안을 검토하여 더 나은 서비스를 제공하겠습니다.',
           '요청하신 서비스는 가능합니다. 자세한 내용은 담당자가 개별 연락드리겠습니다.',
           '결제 관련 문의는 고객센터(1588-1234)로 연락주시면 즉시 처리해드리겠습니다.',
           '환불 요청건에 대해 검토 후 빠른 시일 내에 처리해드리겠습니다.',
           '매니저 변경은 가능합니다. 고객센터를 통해 신청해주시기 바랍니다.',
           '서비스 시간 변경은 예약 시간 2시간 전까지 가능합니다.',
           '정기 서비스 신청 시 할인 혜택이 있습니다. 자세한 내용은 앱에서 확인해주세요.',
           '특별 서비스 요청은 사전 협의를 통해 가능여부를 확인해드립니다.',
           '보험은 모든 서비스에 적용됩니다. 사고 발생 시 즉시 신고해주세요.',
           '앱 관련 오류는 최신 버전으로 업데이트 후 재시도해주세요.',
           '포인트는 결제 시 자동으로 적용되며, 수동으로도 사용 가능합니다.',
           '리뷰 작성 시 500포인트가 자동 적립됩니다.',
           '서비스 지역은 지속적으로 확대하고 있습니다. 새로운 지역 추가 시 알림드리겠습니다.'
       )                                                          as content,
       DATE_ADD(b.created_at, INTERVAL FLOOR(RAND() * 7) + 1 DAY) as created_at,
       NOW()                                                      as updated_at
FROM board b
WHERE b.is_answered = true
  AND RAND() > 0.4 -- 답변된 게시글 중 60%에 실제 답변 내용 추가
LIMIT 1200;

-- ==============================
-- Matching 대용량 더미 데이터 (4000건)
-- ==============================
INSERT INTO matching (reservation_id, manager_id, matching_status, matching_count, created_at, updated_at)
SELECT r.id                                                                     as reservation_id,
       r.manager_id,
       ELT(FLOOR(RAND() * 4) + 1, 'PENDING', 'MATCHED', 'REJECTED', 'CANCELED') as matching_status,
       FLOOR(RAND() * 5)                                                        as matching_count,
       r.created_at                                                             as created_at,
       NOW()                                                                    as updated_at
FROM reservation r
WHERE RAND() > 0.2 -- 80%의 예약에 매칭 정보 생성
LIMIT 4000;

-- ==============================
-- Settlement 대용량 더미 데이터 (3000건)
-- ==============================
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type, platform_fee, amount,
                        create_at, status)
SELECT r.id                                                                     as reservation_id,
       r.manager_id,
       r.service_detail_type_id,
       ELT(FLOOR(RAND() * 3) + 1, 'GENERAL_CLEANING', 'BABYSITTER', 'PET_CARE') as service_type,
       ROUND(r.total_price * 0.2, 2)                                            as platform_fee, -- 플랫폼 수수료 20%
       ROUND(r.total_price * 0.8, 2)                                            as amount,       -- 매니저 정산금액 80%
       DATE_ADD(r.end_time, INTERVAL FLOOR(RAND() * 3) + 1 DAY)                 as create_at,
       ELT(FLOOR(RAND() * 3) + 1, 'PENDING', 'COMPLETED', 'FAILURE')            as status
FROM reservation r
WHERE r.status IN ('COMPLETED', 'WORKING')
  AND RAND() > 0.4 -- 완료된 예약 중 60%에 정산 정보 생성
LIMIT 3000;

-- ==============================
-- Manager Document 대용량 더미 데이터
-- ==============================
INSERT INTO manager_document (manager_id, file_type, file_name, uploaded_file_url, upload_date)
SELECT m.id                                                                                        as manager_id,
       doc_type.file_type,
       CONCAT(m.name, '_', doc_type.file_name)                                                     as file_name,
       'https://d1llec2m3tvk5i.cloudfront.net/uploads/1eab54ed-c5b2-48f4-b7c0-301d788d8350_상장.jpg' as uploaded_file_url,
       DATE_SUB(m.created_at, INTERVAL FLOOR(RAND() * 30) DAY)                                     as upload_date
FROM manager m
         CROSS JOIN (SELECT 'IDENTIFICATION' as file_type, '신분증.jpg' as file_name
                     UNION ALL
                     SELECT 'CRIMINAL_RECORD', '범죄경력조회서.pdf'
                     UNION ALL
                     SELECT 'HEALTH_CERTIFICATE', '건강진단서.pdf'
                     UNION ALL
                     SELECT 'EDUCATION_CERTIFICATE', '학력증명서.pdf'
                     UNION ALL
                     SELECT 'CHILDCARE_CERTIFICATE', '자격증.pdf'
                     UNION ALL
                     SELECT 'PET_CARE_CERTIFICATE', '반려동물관리사자격증.pdf') doc_type
WHERE RAND() > 0.3 -- 각 매니저마다 70% 확률로 각 서류 업로드
LIMIT 2000;

-- ==============================
-- Manager Schedule 대용량 더미 데이터
-- ==============================
INSERT INTO manager_schedule (manager_id, available_day, available_start_time, available_end_time)
SELECT m.id                                           as manager_id,
       days.day_name                                  as available_day,
       TIME(CONCAT(FLOOR(RAND() * 8) + 6, ':00:00'))  as available_start_time, -- 6시-14시 시작
       TIME(CONCAT(FLOOR(RAND() * 6) + 16, ':00:00')) as available_end_time    -- 16시-22시 종료
FROM manager m
         CROSS JOIN (SELECT 'MONDAY' as day_name
                     UNION ALL
                     SELECT 'TUESDAY'
                     UNION ALL
                     SELECT 'WEDNESDAY'
                     UNION ALL
                     SELECT 'THURSDAY'
                     UNION ALL
                     SELECT 'FRIDAY'
                     UNION ALL
                     SELECT 'SATURDAY'
                     UNION ALL
                     SELECT 'SUNDAY') days
WHERE RAND() > 0.3 -- 각 매니저마다 평균 5일 정도 근무
LIMIT 2500;

-- ==============================
-- Manager Service Type 대용량 더미 데이터
-- ==============================
INSERT INTO manager_service (manager_id, service_type)
SELECT m.id              as manager_id,
       FLOOR(RAND() * 3) as service_type -- 0: 청소, 1: 돌봄, 2: 펫케어
FROM manager m
WHERE RAND() > 0.1 -- 90%의 매니저가 최소 1개 서비스 제공
LIMIT 600;

-- 추가 서비스 타입 (일부 매니저는 복수 서비스 제공)
INSERT INTO manager_service (manager_id, service_type)
SELECT m.id              as manager_id,
       FLOOR(RAND() * 3) as service_type
FROM manager m
WHERE RAND() > 0.7 -- 30%의 매니저가 추가 서비스 제공
LIMIT 200;

-- ==============================
-- Manager Preference 대용량 더미 데이터 (3000건)
-- ==============================
INSERT INTO manager_preference (consumer_id, manager_id, preference)
SELECT c.id         as consumer_id,
       m.id         as manager_id,
       RAND() > 0.3 as preference
FROM (SELECT id FROM consumer ORDER BY RAND() LIMIT 3000) c
         JOIN
         (SELECT id FROM manager ORDER BY RAND() LIMIT 3000) m
         ON 1 = 1
WHERE RAND() > 0.5
LIMIT 3000;

-- ==============================
-- Event 대용량 더미 데이터 (50건)
-- ==============================
INSERT INTO event (admin_id, title, main_image_url, image_url, content, created_at, updated_at)
SELECT FLOOR(RAND() * 50) + 1                            as admin_id,
       ELT(FLOOR(RAND() * 20) + 1,
           '신규 회원 포인트 적립 이벤트', '겨울 대청소 할인 이벤트', 'VIP 고객 베이비시터 서비스 무료체험',
           '봄맞이 특별 할인', '여름휴가 펫케어 이벤트', '추석 연휴 특별 서비스',
           '크리스마스 특가 이벤트', '신년 맞이 대청소', '어린이날 베이비시터 할인',
           '어버이날 시니어케어 이벤트', '가정의 달 특별 혜택', '여름방학 아이돌봄',
           '겨울방학 특별 서비스', '새학기 준비 청소', '이사철 특가 이벤트',
           '반려동물 건강검진 이벤트', '정기서비스 할인', '친구추천 이벤트',
           '리뷰이벤트', '앱 다운로드 이벤트'
       )                                                 as title,
       ELT(FLOOR(RAND() * 3) + 1,
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/26a154a2-8db7-4794-bb03-ee5f121463eb_이미지 메인 1.jpg',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/51092621-00c0-47a8-bf4b-7c0b840b2133_이미지 메인 2.jpg',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/07de352b-12c8-4399-b2aa-e3ae4cf5af13_이벤트 2.jpeg'
       )                                                 as main_image_url,
       ELT(FLOOR(RAND() * 3) + 1,
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/3220b607-485e-4c9b-b80e-920513526f05_이미지 메인 1.webp',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/26a154a2-8db7-4794-bb03-ee5f121463eb_이미지 메인 1.jpg',
           'https://d1llec2m3tvk5i.cloudfront.net/uploads/07de352b-12c8-4399-b2aa-e3ae4cf5af13_이벤트 2.jpeg'
       )                                                 as image_url,
       ELT(FLOOR(RAND() * 15) + 1,
           '신규 가입 고객에게 5,000 포인트를 드립니다! 첫 서비스 이용 시 사용 가능합니다.',
           '연말 대청소 서비스 20% 할인! 12월 한정 특가 이벤트입니다.',
           'VIP 등급 고객에게 베이비시터 서비스 2시간 무료 체험 기회를 제공합니다.',
           '봄맞이 특별 할인 이벤트로 모든 서비스 15% 할인됩니다.',
           '여름휴가 기간 중 펫케어 서비스 특별가로 제공합니다.',
           '추석 연휴 기간 특별 서비스를 제공합니다.',
           '크리스마스 특가로 모든 서비스 25% 할인합니다.',
           '신년 맞이 대청소 서비스 패키지를 특가로 제공합니다.',
           '어린이날 기념 베이비시터 서비스 할인 이벤트입니다.',
           '어버이날 기념 시니어케어 서비스 특별가를 제공합니다.',
           '가정의 달 기념 모든 가정 서비스 특별 혜택을 드립니다.',
           '여름방학 기간 아이돌봄 서비스 장기 할인 이벤트입니다.',
           '겨울방학 특별 서비스 패키지를 제공합니다.',
           '새학기 준비를 위한 청소 서비스 할인 이벤트입니다.',
           '이사철 맞이 특가 청소 서비스를 제공합니다.'
       )                                                 as content,
       DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY) as created_at,
       NOW()                                             as updated_at
FROM (SELECT 1 as n
      UNION
      SELECT 2
      UNION
      SELECT 3
      UNION
      SELECT 4
      UNION
      SELECT 5
      UNION
      SELECT 6
      UNION
      SELECT 7
      UNION
      SELECT 8
      UNION
      SELECT 9
      UNION
      SELECT 10) t1,
     (SELECT 1 as n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) t2
LIMIT 47;

-- 특정 유저 데이터 넣기
SELECT *
FROM consumer
WHERE name = '이소은'
  AND is_deleted = false;

-- 1. 매니저 519 예약 데이터 (다양한 상태)
INSERT INTO reservation (manager_id, consumer_id, service_detail_type_id, reservation_date,
                         start_time, end_time, address, address_detail, housing_type, room_size, housing_information,
                         service_add, pet, special_request, total_price, status, canceled_at, checkin_time,
                         checkout_time, final_payment_price, created_at, updated_at)
VALUES
    -- 완료된 예약 1 (청소 서비스)
    (534, 1, 1, '2025-07-15 10:00:00', '2025-07-15 10:00:00', '2025-07-15 14:00:00', '서울시
  강남구 테헤란로 123', '101동 1001호', '아파트', 3, '신축 아파트, 반려동물 없음',
     '냉장고청소,오븐청소', NULL, '특별한 요청사항 없습니다.', 80000.00, 'COMPLETED', NULL,
     '2025-07-15 10:00:00', '2025-07-15 14:00:00', 80000.00, NOW(), NOW()),

    -- 완료된 예약 2 (베이비시터 서비스)
    (534, 2, 3, '2025-07-20 09:00:00', '2025-07-20 09:00:00', '2025-07-20 17:00:00', '서울시
  서초구 서초동 456', '201동 2002호', '아파트', 4, '3살 아이 1명', '놀이활동,급식보조', NULL,
     '아이가 알레르기가 있어서 주의해주세요.', 120000.00, 'COMPLETED', NULL, '2025-07-20
  09:00:00', '2025-07-20 17:00:00', 120000.00, NOW(), NOW()),

    -- 완료된 예약 3 (펫시터 서비스)
    (534, 3, 5, '2025-01-25 16:00:00', '2025-01-25 16:00:00', '2025-01-25 18:00:00', '서울시
  송파구 잠실동 789', '301동 3003호', '아파트', 2, '골든 리트리버 1마리', '산책,놀이',
     '골든리트리버', '산책 시 다른 개들과 놀이 좋아해요.', 50000.00, 'COMPLETED', NULL,
     '2025-01-25 16:00:00', '2025-01-25 18:00:00', 50000.00, NOW(), NOW()),

    -- 진행 중인 예약
    (534, 4, 2, '2025-02-01 13:00:00', '2025-02-01 13:00:00', '2025-02-01 16:00:00', '서울시
  마포구 홍대입구역 근처', '빌라 2층', '빌라', 1, '원룸형 빌라', '화장실청소', NULL, '화장실
  청소를 꼼꼼히 해주세요.', 45000.00, 'WORKING', NULL, '2025-02-01 13:00:00', NULL, 45000.00,
     NOW(), NOW()),

    -- 매칭된 예약
    (534, 5, 4, '2025-02-05 10:00:00', '2025-02-05 10:00:00', '2025-02-05 15:00:00', '서울시
  용산구 한남동 321', '원룸', '원룸', 1, '5살 아이 1명', '학습지도,놀이활동', NULL, '한글
  공부를 시작했어요.', 100000.00, 'MATCHED', NULL, NULL, NULL, 100000.00, NOW(), NOW()),

    -- 결제 완료 예약
    (534, 6, 1, '2025-02-10 14:00:00', '2025-02-10 14:00:00', '2025-02-10 17:00:00', '서울시
  강동구 천호동 123', '다세대주택 3층', '다세대주택', 2, '깨끗한 주택', '침실청소,거실청소',
     NULL, '꼼꼼히 해주세요.', 60000.00, 'PAID', NULL, NULL, NULL, 60000.00, NOW(), NOW()),

    -- 대기 중인 예약
    (534, 7, 6, '2025-02-15 11:00:00', '2025-02-15 11:00:00', '2025-02-15 13:00:00', '서울시
  종로구 인사동 456', '오피스텔 1205호', '오피스텔', 1, '사무실 겸용', '정리정돈', NULL, '빠른
  서비스 부탁드립니다.', 40000.00, 'PENDING', NULL, NULL, NULL, 40000.00, NOW(), NOW()),

    -- 취소된 예약
    (534, 8, 1, '2025-02-20 15:00:00', '2025-02-20 15:00:00', '2025-02-20 18:00:00', '서울시
  성북구 성북동 789', '빌라 1층', '빌라', 2, '반지하 빌라', '전체청소', NULL, '습기 많은 곳이니
   주의해주세요.', 70000.00, 'CANCELED', '2025-02-18 10:00:00', NULL, NULL, 70000.00, NOW(),
     NOW());

-- 2. 매니저 519에 대한 리뷰 데이터
INSERT INTO review (reservation_id, manager_id, consumer_id, rating, comment,
                    service_detail_type_id, review_date, is_consumer_to_manager, created_at, updated_at)
VALUES
    -- 예약 ID는 실제 생성된 ID로 변경 필요 (위 예약들의 ID)
    (10001, 534, 1, 4.8, '정말 깔끔하게 청소해주셔서 감사합니다. 냉장고와 오븐까지 말끔하게
  해주셨어요!', 1, '2025-07-15 15:00:00', true, NOW(), NOW()),
    (10002, 534, 2, 4.9, '아이를 정말 잘 돌봐주셨어요. 안심하고 맡길 수 있었고 아이도 너무
  즐거워했습니다.', 3, '2025-07-20 18:00:00', true, NOW(), NOW()),
    (10003, 534, 3, 4.7, '우리 강아지를 정성스럽게 돌봐주셔서 감사합니다. 산책도 충분히
  해주셨어요.', 5, '2025-07-25 19:00:00', true, NOW(), NOW()),

    -- 매니저가 고객에게 남긴 리뷰
    (10001, 534, 1, 4.5, '깔끔하고 정리정돈이 잘 되어 있어서 작업하기 편했습니다. 좋은
  고객분이셨어요.', 1, '2025-07-15 16:00:00', false, NOW(), NOW()),
    (10002, 534, 2, 4.8, '아이가 정말 예의바르고 착해서 돌보기 수월했습니다. 부모님도
  친절하셨어요.', 3, '2025-07-20 19:00:00', false, NOW(), NOW()),
    (10003, 534, 3, 4.6, '반려동물이 순하고 사람을 잘 따라서 돌보기 좋았습니다. 다음에도 서비스
  받고 싶어요.', 5, '2025-07-25 20:00:00', false, NOW(), NOW());

-- 3. 매니저 519의 평점 및 통계 업데이트
UPDATE manager
SET average_rate       = 4.8,
    total_reviewed_cnt = 3
WHERE id = 534;

-- 5. 매니저 519 서비스 이용 통계용 추가 데이터
INSERT INTO reservation (manager_id, consumer_id, service_detail_type_id, reservation_date,
                         start_time, end_time, address, address_detail, housing_type, room_size, housing_information,
                         service_add, pet, special_request, total_price, status, canceled_at, checkin_time,
                         checkout_time, final_payment_price, created_at, updated_at)
VALUES
    -- 지난달 완료된 예약들 (통계용)
    (534, 10, 1, '2025-07-05 10:00:00', '2025-07-05 10:00:00', '2025-07-05 13:00:00', '서울시
  강남구 역삼동 100', '아파트 15층', '아파트', 3, '깨끗한 아파트', '전체청소', NULL, '꼼꼼히
  해주세요', 75000.00, 'COMPLETED', NULL, '2025-07-05 10:00:00', '2025-01-05 13:00:00',
     75000.00, NOW(), NOW()),
    (534, 11, 2, '2025-07-10 14:00:00', '2025-07-10 14:00:00', '2025-07-10 17:00:00', '서울시
  서초구 잠원동 200', '빌라 2층', '빌라', 2, '조용한 빌라', '침실청소', NULL, '알레르기 있으니
  주의해주세요', 55000.00, 'COMPLETED', NULL, '2025-07-10 14:00:00', '2025-07-10 17:00:00',
     55000.00, NOW(), NOW()),
    (534, 12, 4, '2025-07-12 16:00:00', '2025-07-12 16:00:00', '2025-07-12 20:00:00', '서울시
  마포구 상수동 300', '원룸텔', '원룸', 1, '4살 아이 1명', '놀이활동', NULL, '활발한 아이예요',
     90000.00, 'COMPLETED', NULL, '2025-07-12 16:00:00', '2025-07-12 20:00:00', 90000.00, NOW(),
     NOW());

-- 1. 완료된 예약에 대한 정산 데이터
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 완료된 청소 서비스 정산 (예약 ID 10001)
    (10001, 519, 1, 'GENERAL_CLEANING', 16000.00, 64000.00, '2025-07-16 10:00:00', 'COMPLETED'),

    -- 완료된 베이비시터 서비스 정산 (예약 ID 10002)
    (10002, 519, 3, 'BABYSITTER', 24000.00, 96000.00, '2025-07-21 09:00:00', 'COMPLETED'),

    -- 완료된 펫시터 서비스 정산 (예약 ID 10003)
    (10003, 519, 5, 'PET_CARE', 10000.00, 40000.00, '2025-07-26 16:00:00', 'COMPLETED'),

    -- 지난달 완료된 청소 서비스 정산
    (10009, 519, 1, 'GENERAL_CLEANING', 15000.00, 60000.00, '2025-07-06 10:00:00', 'COMPLETED'),

    -- 지난달 완료된 청소 서비스 정산 2
    (10010, 519, 2, 'GENERAL_CLEANING', 11000.00, 44000.00, '2025-07-11 14:00:00', 'COMPLETED'),

    -- 지난달 완료된 베이비시터 서비스 정산
    (10011, 519, 4, 'BABYSITTER', 18000.00, 72000.00, '2025-07-13 16:00:00', 'COMPLETED'),

    -- 대기 중인 정산 (진행 중인 예약 관련)
    (10004, 519, 2, 'GENERAL_CLEANING', 9000.00, 36000.00, '2025-08-02 13:00:00', 'PENDING'),

    -- 실패한 정산 (재시도 필요)
    (10012, 519, 1, 'GENERAL_CLEANING', 12000.00, 48000.00, '2025-07-15 10:00:00', 'FAILURE');

-- 4. 매니저 519 정산 상세 내역 (주간별)
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 1주차 정산
    (10013, 519, 1, 'GENERAL_CLEANING', 14000.00, 56000.00, '2025-07-03 14:00:00', 'COMPLETED'),
    (10014, 519, 3, 'BABYSITTER', 22000.00, 88000.00, '2025-07-04 18:00:00', 'COMPLETED'),

    -- 2주차 정산
    (10015, 519, 2, 'GENERAL_CLEANING', 13000.00, 52000.00, '2025-07-09 11:00:00', 'COMPLETED'),
    (10016, 519, 5, 'PET_CARE', 8000.00, 32000.00, '2025-07-11 17:00:00', 'COMPLETED'),

    -- 3주차 정산
    (10017, 519, 1, 'GENERAL_CLEANING', 16000.00, 64000.00, '2025-07-17 13:00:00', 'COMPLETED'),
    (10018, 519, 4, 'BABYSITTER', 20000.00, 80000.00, '2025-07-19 19:00:00', 'COMPLETED'),

    -- 4주차 정산
    (10019, 519, 2, 'GENERAL_CLEANING', 12000.00, 48000.00, '2025-07-24 12:00:00', 'COMPLETED'),
    (10020, 519, 6, 'OTHER', 10000.00, 40000.00, '2025-07-26 15:00:00', 'COMPLETED');

-- 5. 매니저 519 정산 실패 및 재시도 데이터
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 정산 실패 (계좌 정보 오류)
    (10021, 519, 1, 'GENERAL_CLEANING', 15000.00, 60000.00, '2025-01-28 10:00:00', 'FAILURE'),

    -- 정산 실패 (시스템 오류)
    (10022, 519, 3, 'BABYSITTER', 18000.00, 72000.00, '2025-01-29 14:00:00', 'FAILURE'),

    -- 대기 중인 정산들
    (10023, 519, 2, 'GENERAL_CLEANING', 11000.00, 44000.00, '2025-02-01 09:00:00', 'PENDING'),
    (10024, 519, 5, 'PET_CARE', 9000.00, 36000.00, '2025-02-03 16:00:00', 'PENDING');

 -- 1. 이번주 완료된 예약에 대한 정산 데이터
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 월요일 (2025-07-15) 완료된 청소 서비스 정산
    (20001, 519, 1, 'GENERAL_CLEANING', 18000.00, 72000.00, '2025-07-15 14:30:00', 'COMPLETED'),

    -- 화요일 (2025-07-16) 완료된 베이비시터 서비스 정산
    (20002, 519, 3, 'BABYSITTER', 25000.00, 100000.00, '2025-07-16 19:00:00', 'COMPLETED'),

    -- 수요일 (2025-07-17) 완료된 펫시터 서비스 정산
    (20003, 519, 5, 'PET_CARE', 12000.00, 48000.00, '2025-07-17 18:30:00', 'COMPLETED'),

    -- 목요일 (2025-07-18) 완료된 청소 서비스 정산
    (20004, 519, 2, 'GENERAL_CLEANING', 14000.00, 56000.00, '2025-07-18 16:00:00', 'COMPLETED'),

    -- 금요일 (2025-07-19) 완료된 베이비시터 서비스 정산
    (20005, 519, 4, 'BABYSITTER', 22000.00, 88000.00, '2025-07-19 20:00:00', 'COMPLETED'),

    -- 토요일 (2025-07-20) 완료된 청소 서비스 정산
    (20006, 519, 1, 'GENERAL_CLEANING', 16000.00, 64000.00, '2025-07-20 15:30:00', 'COMPLETED'),

    -- 일요일 (2025-07-21) 완료된 펫시터 서비스 정산
    (20007, 519, 5, 'PET_CARE', 10000.00, 40000.00, '2025-07-21 17:00:00', 'COMPLETED');

-- 2. 이번주 대기 중인 정산 (오늘 완료된 서비스)
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 오늘 (2025-07-17) 오후에 완료된 서비스 - 아직 정산 대기 중
    (20008, 519, 1, 'GENERAL_CLEANING', 15000.00, 60000.00, '2025-07-17 15:00:00', 'PENDING'),

    -- 어제 (2025-07-16) 늦은 시간 완료된 서비스 - 정산 대기 중
    (20009, 519, 3, 'BABYSITTER', 20000.00, 80000.00, '2025-07-16 22:00:00', 'PENDING');

-- 3. 이번주 보너스 정산
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 주간 우수 매니저 보너스 (고평점 유지)
    (NULL, 519, NULL, 'WEEKLY_BONUS', 0.00, 30000.00, '2025-07-15 09:00:00', 'COMPLETED'),

    -- 연속 서비스 완료 보너스
    (NULL, 519, NULL, 'STREAK_BONUS', 0.00, 15000.00, '2025-07-18 10:00:00', 'COMPLETED'),

    -- 신규 고객 유치 보너스
    (NULL, 519, NULL, 'REFERRAL_BONUS', 0.00, 25000.00, '2025-07-19 11:00:00', 'COMPLETED');

-- 4. 이번주 정산 실패 건 (재시도 필요)
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 계좌 정보 오류로 실패
    (20010, 519, 2, 'GENERAL_CLEANING', 13000.00, 52000.00, '2025-07-16 10:00:00', 'FAILURE'),

    -- 시스템 오류로 실패 (재시도 예정)
    (20011, 519, 4, 'BABYSITTER', 18000.00, 72000.00, '2025-07-17 14:00:00', 'FAILURE');

-- 5. 이번주 추가 서비스 정산
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 긴급 서비스 추가 요금 정산
    (20012, 519, 1, 'EMERGENCY_CLEANING', 8000.00, 32000.00, '2025-07-15 21:00:00', 'COMPLETED'),

    -- 야간 서비스 추가 요금 정산
    (20013, 519, 3, 'NIGHT_BABYSITTER', 12000.00, 48000.00, '2025-07-16 23:30:00', 'COMPLETED'),

    -- 주말 서비스 추가 요금 정산
    (20014, 519, 5, 'WEEKEND_PET_CARE', 9000.00, 36000.00, '2025-07-20 19:00:00', 'COMPLETED');

-- 6. 이번주 정산 통계 조회 쿼리 (참고용)
SELECT '이번주 정산 현황'                        as category,
       DATE_FORMAT(create_at, '%Y-%m-%d') as date,
       service_type,
       COUNT(*)                           as settlement_count,
       SUM(platform_fee)                  as total_platform_fee,
       SUM(amount)                        as total_amount,
       status
FROM settlement
WHERE manager_id = 519
  AND create_at >= '2025-07-15 00:00:00'
  AND create_at <= '2025-07-21 23:59:59'
GROUP BY DATE_FORMAT(create_at, '%Y-%m-%d'), service_type, status
ORDER BY date DESC, service_type;

-- 7. 이번주 일별 정산 요약
SELECT DAYNAME(create_at)                                         as day_name,
       DATE_FORMAT(create_at, '%Y-%m-%d')                         as date,
       COUNT(*)                                                   as total_settlements,
       SUM(CASE WHEN status = 'COMPLETED' THEN amount ELSE 0 END) as completed_amount,
       SUM(CASE WHEN status = 'PENDING' THEN amount ELSE 0 END)   as pending_amount,
       SUM(CASE WHEN status = 'FAILURE' THEN amount ELSE 0 END)   as failed_amount
FROM settlement
WHERE manager_id = 519
  AND create_at >= '2025-07-15 00:00:00'
  AND create_at <= '2025-07-21 23:59:59'
GROUP BY DATE_FORMAT(create_at, '%Y-%m-%d')
ORDER BY date;
-- 1. 이번주 완료된 예약에 대한 정산 데이터
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 월요일 (2025-07-14) 완료된 청소 서비스 정산
    (20001, 519, 1, 'GENERAL_CLEANING', 18000.00, 72000.00, '2025-07-14 14:30:00', 'COMPLETED'),

    -- 화요일 (2025-07-15) 완료된 베이비시터 서비스 정산
    (20002, 519, 3, 'BABYSITTER', 25000.00, 100000.00, '2025-07-15 19:00:00', 'COMPLETED'),

    -- 수요일 (2025-07-16) 완료된 펫시터 서비스 정산
    (20003, 519, 5, 'PET_CARE', 12000.00, 48000.00, '2025-07-16 18:30:00', 'COMPLETED'),

    -- 목요일 (2025-07-17) 완료된 청소 서비스 정산 (오늘)
    (20004, 519, 2, 'GENERAL_CLEANING', 14000.00, 56000.00, '2025-07-17 16:00:00', 'COMPLETED'),

    -- 금요일 (2025-07-18) 완료 예정 베이비시터 서비스 정산
    (20005, 519, 4, 'BABYSITTER', 22000.00, 88000.00, '2025-07-18 20:00:00', 'PENDING'),

    -- 토요일 (2025-07-19) 예정된 청소 서비스 정산
    (20006, 519, 1, 'GENERAL_CLEANING', 16000.00, 64000.00, '2025-07-19 15:30:00', 'PENDING'),

    -- 일요일 (2025-07-20) 예정된 펫시터 서비스 정산
    (20007, 519, 5, 'PET_CARE', 10000.00, 40000.00, '2025-07-20 17:00:00', 'PENDING');

-- 2. 오늘 (2025-07-17) 완료된 서비스 - 정산 대기 중
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 오늘 오후에 완료된 추가 서비스 - 아직 정산 대기 중
    (20008, 519, 1, 'GENERAL_CLEANING', 15000.00, 60000.00, '2025-07-17 15:00:00', 'PENDING'),

    -- 어제 (2025-07-16) 늦은 시간 완료된 서비스 - 정산 대기 중
    (20009, 519, 3, 'BABYSITTER', 20000.00, 80000.00, '2025-07-16 22:00:00', 'PENDING');

-- 3. 이번주 보너스 정산
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 주간 우수 매니저 보너스 (고평점 유지)
    (NULL, 519, NULL, 'WEEKLY_BONUS', 0.00, 30000.00, '2025-07-14 09:00:00', 'COMPLETED'),

    -- 연속 서비스 완료 보너스
    (NULL, 519, NULL, 'STREAK_BONUS', 0.00, 15000.00, '2025-07-16 10:00:00', 'COMPLETED'),

    -- 신규 고객 유치 보너스
    (NULL, 519, NULL, 'REFERRAL_BONUS', 0.00, 25000.00, '2025-07-17 11:00:00', 'COMPLETED');

-- 4. 이번주 정산 실패 건 (재시도 필요)
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 계좌 정보 오류로 실패
    (20010, 519, 2, 'GENERAL_CLEANING', 13000.00, 52000.00, '2025-07-15 10:00:00', 'FAILURE'),

    -- 시스템 오류로 실패 (재시도 예정)
    (20011, 519, 4, 'BABYSITTER', 18000.00, 72000.00, '2025-07-16 14:00:00', 'FAILURE');

-- 5. 이번주 추가 서비스 정산
INSERT INTO settlement (reservation_id, manager_id, service_detail_type_id, service_type,
                        platform_fee, amount, create_at, status)
VALUES
    -- 긴급 서비스 추가 요금 정산
    (20012, 519, 1, 'EMERGENCY_CLEANING', 8000.00, 32000.00, '2025-07-14 21:00:00', 'COMPLETED'),

    -- 야간 서비스 추가 요금 정산
    (20013, 519, 3, 'NIGHT_BABYSITTER', 12000.00, 48000.00, '2025-07-15 23:30:00', 'COMPLETED'),

    -- 주말 서비스 추가 요금 정산 (예정)
    (20014, 519, 5, 'WEEKEND_PET_CARE', 9000.00, 36000.00, '2025-07-19 19:00:00', 'PENDING');

-- 6. 이번주 정산 통계 조회 쿼리 (참고용)
SELECT '이번주 정산 현황'                        as category,
       DATE_FORMAT(create_at, '%Y-%m-%d') as date,
       service_type,
       COUNT(*)                           as settlement_count,
       SUM(platform_fee)                  as total_platform_fee,
       SUM(amount)                        as total_amount,
       status
FROM settlement
WHERE manager_id = 519
  AND create_at >= '2025-07-14 00:00:00'
  AND create_at <= '2025-07-20 23:59:59'
GROUP BY DATE_FORMAT(create_at, '%Y-%m-%d'), service_type, status
ORDER BY date DESC, service_type;

-- 7. 이번주 일별 정산 요약
SELECT DAYNAME(create_at)                                         as day_name,
       DATE_FORMAT(create_at, '%Y-%m-%d')                         as date,
       COUNT(*)                                                   as total_settlements,
       SUM(CASE WHEN status = 'COMPLETED' THEN amount ELSE 0 END) as completed_amount,
       SUM(CASE WHEN status = 'PENDING' THEN amount ELSE 0 END)   as pending_amount,
       SUM(CASE WHEN status = 'FAILURE' THEN amount ELSE 0 END)   as failed_amount
FROM settlement
WHERE manager_id = 519
  AND create_at >= '2025-07-14 00:00:00'
  AND create_at <= '2025-07-20 23:59:59'
GROUP BY DATE_FORMAT(create_at, '%Y-%m-%d')
ORDER BY date;
-- 1. 과거 완료된 예약 (지난주 - 이번주 초)
INSERT INTO reservation (manager_id, consumer_id, service_detail_type_id, reservation_date,
                         start_time, end_time, address, address_detail, housing_type, room_size, housing_information,
                         service_add, pet, special_request, total_price, status, canceled_at, checkin_time,
                         checkout_time, final_payment_price, created_at, updated_at)
VALUES
    -- 지난주 완료된 예약들
    (519, 1, 1, '2025-07-07 10:00:00', '2025-07-07 10:00:00', '2025-07-07 14:00:00', '서울시
  강남구 테헤란로 123', '101동 1001호', '아파트', 3, '신축 아파트, 반려동물 없음',
     '냉장고청소,오븐청소', NULL, '특별한 요청사항 없습니다.', 90000.00, 'COMPLETED', NULL,
     '2025-07-07 10:00:00', '2025-07-07 14:00:00', 90000.00, '2025-07-01 09:00:00', '2025-07-07
  14:00:00'),

    (519, 2, 3, '2025-07-09 09:00:00', '2025-07-09 09:00:00', '2025-07-09 17:00:00', '서울시
  서초구 서초동 456', '201동 2002호', '아파트', 4, '5살 아이 1명', '놀이활동,급식보조', NULL,
     '아이가 알레르기가 있어서 주의해주세요.', 125000.00, 'COMPLETED', NULL, '2025-07-09
  09:00:00', '2025-07-09 17:00:00', 125000.00, '2025-07-02 14:00:00', '2025-07-09 17:00:00'),

    (519, 3, 5, '2025-07-11 16:00:00', '2025-07-11 16:00:00', '2025-07-11 18:00:00', '서울시
  송파구 잠실동 789', '301동 3003호', '아파트', 2, '골든 리트리버 1마리', '산책,놀이',
     '골든리트리버', '산책 시 다른 개들과 놀이 좋아해요.', 60000.00, 'COMPLETED', NULL,
     '2025-07-11 16:00:00', '2025-07-11 18:00:00', 60000.00, '2025-07-05 11:00:00', '2025-07-11
  18:00:00'),

    -- 이번주 월요일 완료
    (519, 4, 2, '2025-07-14 13:00:00', '2025-07-14 13:00:00', '2025-07-14 16:00:00', '서울시
  마포구 홍대입구역 근처', '빌라 2층', '빌라', 1, '원룸형 빌라', '화장실청소', NULL, '화장실
  청소를 꼼꼼히 해주세요.', 70000.00, 'COMPLETED', NULL, '2025-07-14 13:00:00', '2025-07-14
  16:00:00', 70000.00, '2025-07-10 10:00:00', '2025-07-14 16:00:00'),

    -- 이번주 화요일 완료
    (519, 5, 4, '2025-07-15 10:00:00', '2025-07-15 10:00:00', '2025-07-15 15:00:00', '서울시
  용산구 한남동 321', '원룸', '원룸', 1, '3살 아이 1명', '학습지도,놀이활동', NULL, '한글
  공부를 시작했어요.', 110000.00, 'COMPLETED', NULL, '2025-07-15 10:00:00', '2025-07-15
  15:00:00', 110000.00, '2025-07-11 15:00:00', '2025-07-15 15:00:00'),

    -- 이번주 수요일 완료
    (519, 6, 1, '2025-07-16 14:00:00', '2025-07-16 14:00:00', '2025-07-16 17:00:00', '서울시
  강동구 천호동 123', '다세대주택 3층', '다세대주택', 2, '깨끗한 주택', '침실청소,거실청소',
     NULL, '꼼꼼히 해주세요.', 80000.00, 'COMPLETED', NULL, '2025-07-16 14:00:00', '2025-07-16
  17:00:00', 80000.00, '2025-07-12 09:00:00', '2025-07-16 17:00:00');

-- 2. 현재 진행 중인 예약 (오늘 2025-07-17)
INSERT INTO reservation (manager_id, consumer_id, service_detail_type_id, reservation_date,
                         start_time, end_time, address, address_detail, housing_type, room_size, housing_information,
                         service_add, pet, special_request, total_price, status, canceled_at, checkin_time,
                         checkout_time, final_payment_price, created_at, updated_at)
VALUES
    -- 오늘 오후 진행 중인 예약
    (519, 7, 2, '2025-07-17 14:00:00', '2025-07-17 14:00:00', '2025-07-17 17:00:00', '서울시
  종로구 인사동 456', '오피스텔 1205호', '오피스텔', 1, '사무실 겸용', '정리정돈,청소', NULL,
     '빠른 서비스 부탁드립니다.', 75000.00, 'WORKING', NULL, '2025-07-17 14:00:00', NULL,
     75000.00, '2025-07-13 16:00:00', '2025-07-17 14:00:00'),

    -- 오늘 저녁 예정된 예약
    (519, 8, 3, '2025-07-17 18:00:00', '2025-07-17 18:00:00', '2025-07-17 21:00:00', '서울시
  성북구 성북동 789', '아파트 B동 503호', '아파트', 3, '7살 아이 1명', '놀이활동,숙제도움',
     NULL, '아이가 활발해서 에너지가 많아요.', 90000.00, 'MATCHED', NULL, NULL, NULL, 90000.00,
     '2025-07-14 12:00:00', '2025-07-17 12:00:00');

-- 3. 향후 예약 (내일 이후)
INSERT INTO reservation (manager_id, consumer_id, service_detail_type_id, reservation_date,
                         start_time, end_time, address, address_detail, housing_type, room_size, housing_information,
                         service_add, pet, special_request, total_price, status, canceled_at, checkin_time,
                         checkout_time, final_payment_price, created_at, updated_at)
VALUES
    -- 내일 (2025-07-18) 예약
    (519, 9, 1, '2025-07-18 10:00:00', '2025-07-18 10:00:00', '2025-07-18 13:00:00', '서울시
  강서구 화곡동 100', '빌라 1층', '빌라', 2, '반지하 빌라', '전체청소', NULL, '습기 많은 곳이니
   주의해주세요.', 85000.00, 'PAID', NULL, NULL, NULL, 85000.00, '2025-07-15 14:00:00',
     '2025-07-16 10:00:00'),

    -- 모레 (2025-07-19) 예약
    (519, 10, 5, '2025-07-19 15:00:00', '2025-07-19 15:00:00', '2025-07-19 17:00:00', '서울시
  관악구 신림동 200', '원룸텔 502호', '원룸', 1, '고양이 2마리', '놀이,급식', '페르시안고양이',
     '고양이들이 예민해서 조용히 해주세요.', 55000.00, 'MATCHED', NULL, NULL, NULL, 55000.00,
     '2025-07-16 18:00:00', '2025-07-17 09:00:00'),

    -- 이번주 토요일 (2025-07-19) 예약
    (519, 11, 4, '2025-07-19 09:00:00', '2025-07-19 09:00:00', '2025-07-19 13:00:00', '서울시
  노원구 상계동 300', '아파트 15층', '아파트', 4, '4살, 6살 아이 2명', '놀이활동,간식준비',
     NULL, '아이들이 서로 잘 싸워요.', 120000.00, 'PAID', NULL, NULL, NULL, 120000.00, '2025-07-17
   10:00:00', '2025-07-17 11:00:00'),

    -- 이번주 일요일 (2025-07-20) 예약
    (519, 12, 1, '2025-07-20 14:00:00', '2025-07-20 14:00:00', '2025-07-20 17:00:00', '서울시
  동작구 사당동 400', '오피스텔 1008호', '오피스텔', 1, '깨끗한 오피스텔', '전체청소', NULL,
     '이사 전 청소 부탁드립니다.', 95000.00, 'MATCHED', NULL, NULL, NULL, 95000.00, '2025-07-16
  20:00:00', '2025-07-17 08:00:00'),

    -- 다음주 예약
    (519, 13, 2, '2025-07-21 11:00:00', '2025-07-21 11:00:00', '2025-07-21 14:00:00', '서울시
  중구 명동 500', '아파트 A동 1203호', '아파트', 3, '새 아파트', '입주청소', NULL, '입주 전
  청소 부탁드립니다.', 100000.00, 'PENDING', NULL, NULL, NULL, 100000.00, '2025-07-17
  13:00:00', '2025-07-17 13:00:00'),

    (519, 14, 3, '2025-07-22 16:00:00', '2025-07-22 16:00:00', '2025-07-22 20:00:00', '서울시
  영등포구 여의도동 600', '아파트 C동 2501호', '아파트', 4, '3살 아이 1명', '놀이활동,목욕',
     NULL, '아이가 목욕을 좋아해요.', 100000.00, 'PENDING', NULL, NULL, NULL, 100000.00,
     '2025-07-17 15:00:00', '2025-07-17 15:00:00');

-- 4. 최근 취소된 예약
INSERT INTO reservation (manager_id, consumer_id, service_detail_type_id, reservation_date,
                         start_time, end_time, address, address_detail, housing_type, room_size, housing_information,
                         service_add, pet, special_request, total_price, status, canceled_at, checkin_time,
                         checkout_time, final_payment_price, created_at, updated_at)
VALUES
    -- 어제 취소된 예약
    (519, 15, 1, '2025-07-16 10:00:00', '2025-07-16 10:00:00', '2025-07-16 13:00:00', '서울시
  구로구 구로동 700', '빌라 3층', '빌라', 2, '오래된 빌라', '전체청소', NULL, '철저한 청소
  부탁드립니다.', 78000.00, 'CANCELED', '2025-07-15 18:00:00', NULL, NULL, 78000.00,
     '2025-07-12 11:00:00', '2025-07-15 18:00:00'),

    -- 오늘 오전에 취소된 예약
    (519, 16, 5, '2025-07-17 13:00:00', '2025-07-17 13:00:00', '2025-07-17 15:00:00', '서울시
  금천구 가산동 800', '원룸텔 302호', '원룸', 1, '강아지 1마리', '산책,놀이', '비글', '활발한
  강아지입니다.', 45000.00, 'CANCELED', '2025-07-17 10:00:00', NULL, NULL, 45000.00,
     '2025-07-14 16:00:00', '2025-07-17 10:00:00');
