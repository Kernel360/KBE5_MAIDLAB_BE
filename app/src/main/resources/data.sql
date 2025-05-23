INSERT INTO service_detail_type (id,service_detail_type,service_type)
SELECT 1, '대청소','HOUSEKEEPING' WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 1)
UNION ALL
SELECT 2, '부분청소','HOUSEKEEPING' WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 2)
UNION ALL
SELECT 3, '아이돌봄','CARE' WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 3)
UNION ALL
SELECT 4, '어르신돌봄','CARE' WHERE NOT EXISTS (SELECT 1 FROM service_detail_type WHERE id = 4);