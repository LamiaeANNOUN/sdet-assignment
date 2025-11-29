CREATE TABLE IF NOT EXISTS deals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    deal_unique_id VARCHAR(255) NOT NULL UNIQUE,
    ordering_currency CHAR(3) NOT NULL,
    counter_currency CHAR(3) NOT NULL,
    deal_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    amount NUMERIC(19,4) NOT NULL
);
