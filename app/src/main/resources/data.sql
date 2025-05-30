INSERT INTO service_detail_type (id, service_detail_type, service_type, service_price)
SELECT 1, '대청소', 'HOUSEKEEPING', 50000
WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 1)
UNION ALL
SELECT 2, '부분청소', 'HOUSEKEEPING', 30000
WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 2)
UNION ALL
SELECT 3, '기타청소', 'HOUSEKEEPING', 20000
WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 3)
UNION ALL
SELECT 4, '아이돌봄', 'CARE', 50000
WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 4)
UNION ALL
SELECT 5, '어르신돌봄', 'CARE', 30000
WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 5);
INSERT INTO region (region)
SELECT '강남구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '강남구');
INSERT INTO region (region)
SELECT '강동구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '강동구');
INSERT INTO region (region)
SELECT '강북구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '강북구');
INSERT INTO region (region)
SELECT '강서구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '강서구');
INSERT INTO region (region)
SELECT '관악구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '관악구');
INSERT INTO region (region)
SELECT '광진구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '광진구');
INSERT INTO region (region)
SELECT '구로구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '구로구');
INSERT INTO region (region)
SELECT '금천구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '금천구');
INSERT INTO region (region)
SELECT '노원구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '노원구');
INSERT INTO region (region)
SELECT '도봉구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '도봉구');
INSERT INTO region (region)
SELECT '동대문구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '동대문구');
INSERT INTO region (region)
SELECT '동작구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '동작구');
INSERT INTO region (region)
SELECT '마포구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '마포구');
INSERT INTO region (region)
SELECT '서대문구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '서대문구');
INSERT INTO region (region)
SELECT '서초구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '서초구');
INSERT INTO region (region)
SELECT '성동구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '성동구');
INSERT INTO region (region)
SELECT '성북구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '성북구');
INSERT INTO region (region)
SELECT '송파구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '송파구');
INSERT INTO region (region)
SELECT '양천구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '양천구');
INSERT INTO region (region)
SELECT '영등포구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '영등포구');
INSERT INTO region (region)
SELECT '용산구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '용산구');
INSERT INTO region (region)
SELECT '은평구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '은평구');
INSERT INTO region (region)
SELECT '종로구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '종로구');
INSERT INTO region (region)
SELECT '중구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '중구');
INSERT INTO region (region)
SELECT '중랑구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region = '중랑구');
