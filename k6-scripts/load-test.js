import http from 'k6/http';
import { check } from 'k6';

const currencies = ['USD', 'EUR', 'GBP', 'JPY', 'CHF', 'CAD', 'AUD'];

function randomCurrency() {
  return currencies[Math.floor(Math.random() * currencies.length)];
}
function randomAmount() {
  return (Math.random() * 10000 + 1).toFixed(2);
}
function randomTimestamp() {
  return new Date().toISOString();
}
function randomDealId() {
  return 'K6-' + Math.random().toString(36).substring(2, 10);
}

// ✅ Generate CSV in EXACT ORDER your controller expects
function generateCsv(numRows) {
  let csv = 'dealUniqueId,orderingCurrency,counterCurrency,amount,dealTimestamp\n';
  for (let i = 0; i < numRows; i++) {
    const from = randomCurrency();
    let to;
    do { to = randomCurrency(); } while (to === from);

    csv += `${randomDealId()},${from},${to},${randomAmount()},${randomTimestamp()}\n`;
  }
  return csv;
}

export const options = {
  vus: 5,
  iterations: 50,
  thresholds: {
    http_req_failed: ['rate < 0.01'],
    http_req_duration: ['p(95) < 500'],
  },
};

export default function () {
  const csvData = generateCsv(10);

  const formData = {
    file: http.file(csvData, 'deals.csv', 'text/csv'),
  };

  const res = http.post('http://localhost:8080/api/deals/import', formData);

  check(res, {
    'status 200 or 207': (r) => [200, 207].includes(r.status),
    'at least 1 row succeeded': (r) => JSON.parse(r.body).some(row => row.success === true),
  });

  console.log(res.body);
}
