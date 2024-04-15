--************************************************************分割线************************************************************

local result = {}

if KEYS[1] == "1" then
    result["key"] = KEYS[1]
end
if ARGV[1] == "2" then
    result["val"] = tonumber(ARGV[1])
end

return cjson.encode(result)
