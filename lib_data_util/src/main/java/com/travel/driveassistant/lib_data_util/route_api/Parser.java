package com.travel.driveassistant.lib_data_util.route_api;

import java.util.List;

//. by Haseem Saheed
public interface Parser {
    List<Route> parse() throws RouteException;
}