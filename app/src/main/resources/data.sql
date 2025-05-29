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
INSERT INTO region (region_name)
SELECT '강남구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '강남구');
INSERT INTO region (region_name)
SELECT '강동구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '강동구');
INSERT INTO region (region_name)
SELECT '강북구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '강북구');
INSERT INTO region (region_name)
SELECT '강서구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '강서구');
INSERT INTO region (region_name)
SELECT '관악구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '관악구');
INSERT INTO region (region_name)
SELECT '광진구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '광진구');
INSERT INTO region (region_name)
SELECT '구로구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '구로구');
INSERT INTO region (region_name)
SELECT '금천구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '금천구');
INSERT INTO region (region_name)
SELECT '노원구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '노원구');
INSERT INTO region (region_name)
SELECT '도봉구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '도봉구');
INSERT INTO region (region_name)
SELECT '동대문구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '동대문구');
INSERT INTO region (region_name)
SELECT '동작구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '동작구');
INSERT INTO region (region_name)
SELECT '마포구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '마포구');
INSERT INTO region (region_name)
SELECT '서대문구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '서대문구');
INSERT INTO region (region_name)
SELECT '서초구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '서초구');
INSERT INTO region (region_name)
SELECT '성동구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '성동구');
INSERT INTO region (region_name)
SELECT '성북구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '성북구');
INSERT INTO region (region_name)
SELECT '송파구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '송파구');
INSERT INTO region (region_name)
SELECT '양천구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '양천구');
INSERT INTO region (region_name)
SELECT '영등포구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '영등포구');
INSERT INTO region (region_name)
SELECT '용산구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '용산구');
INSERT INTO region (region_name)
SELECT '은평구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '은평구');
INSERT INTO region (region_name)
SELECT '종로구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '종로구');
INSERT INTO region (region_name)
SELECT '중구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '중구');
INSERT INTO region (region_name)
SELECT '중랑구' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM region WHERE region_name = '중랑구');
