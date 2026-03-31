#!/bin/bash
# Script do thoi gian 3 cau hinh cho bang hieu suat

GRID_URL="http://localhost:4444"

echo "======================================"
echo "BENCHMARK: Selenium Grid Performance"
echo "======================================"

curl -s "$GRID_URL/wd/hub/status" | grep -q '"ready":true' || {
    echo "ERROR: Grid chua chay. Chay 'docker-compose up -d' truoc."
    exit 1
}

echo ""
echo "[1/3] Tuan tu - 1 thread (local)..."
START=$(date +%s%N)
mvn test -DsuiteXmlFile=testng-sequential.xml -Denv=dev -q 2>/dev/null
END=$(date +%s%N)
SEQ_TIME=$(( (END - START) / 1000000 ))
echo "    -> Thoi gian: ${SEQ_TIME}ms"

echo ""
echo "[2/3] Song song Grid - 2 thread..."
START=$(date +%s%N)
mvn test -Dgrid.url=$GRID_URL -DsuiteXmlFile=testng-grid-2thread.xml -Denv=dev -q 2>/dev/null
END=$(date +%s%N)
GRID2_TIME=$(( (END - START) / 1000000 ))
echo "    -> Thoi gian: ${GRID2_TIME}ms"

echo ""
echo "[3/3] Song song Grid - 4 thread..."
START=$(date +%s%N)
mvn test -Dgrid.url=$GRID_URL -DsuiteXmlFile=testng-grid.xml -Denv=dev -q 2>/dev/null
END=$(date +%s%N)
GRID4_TIME=$(( (END - START) / 1000000 ))
echo "    -> Thoi gian: ${GRID4_TIME}ms"

echo ""
echo "======================================"cls

echo "KET QUA BENCHMARK"
echo "======================================"
printf "%-30s %-10s %-15s %-15s\n" "Cau hinh" "Thread" "Thoi gian(ms)" "He so tang toc"
printf "%-30s %-10s %-15s %-15s\n" "Tuan tu (local)" "1" "${SEQ_TIME}" "1.0x"
printf "%-30s %-10s %-15s %-15s\n" "Song song Grid" "2" "${GRID2_TIME}" "$(echo "scale=1; $SEQ_TIME/$GRID2_TIME" | bc)x"
printf "%-30s %-10s %-15s %-15s\n" "Song song Grid" "4" "${GRID4_TIME}" "$(echo "scale=1; $SEQ_TIME/$GRID4_TIME" | bc)x"