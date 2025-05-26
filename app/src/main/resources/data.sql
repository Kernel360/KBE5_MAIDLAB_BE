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
