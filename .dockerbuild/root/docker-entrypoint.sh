#!/usr/bin/env bash

#
# Banner
#

echo '
--------------------------------------------------------------------
           _____ __  __  _____    _____           _
     /\   |_   _|  \/  |/ ____|  / ____|         | |
    /  \    | | | \  / | (___   | (___  _   _ ___| |_ ___ _ __ ___
   / /\ \   | | | |\/| |\___ \   \___ \| | | / __| __/ _ \  _ ` _ \
  / ____ \ _| |_| |  | |____) |  ____) | |_| \__ \ ||  __/ | | | | |
 /_/    \_\_____|_|  |_|_____/  |_____/ \__, |___/\__\___|_| |_| |_|
                                         __/ |
                                        |___/
Brought to you by solumesl.com

'

echo "
--------------------------------------------------------------------------
Unprivileged user
--------------------------------------------------------------------------
User name:   aims
User uid:    $(id -u aims)
User gid:    $(id -g aims)

"

#
# Functions
#

replace_with_old_variable_value() {
    #
    # Description:
    #   Replace old variable value with new empty variable value
    #
    # Usage:
    #   replace_with_old_variable_value old_variable new_variable
    #
    # Example:
    #   DATABASE_URL="jdbc:postgresql://127.0.0.1:6010/AIMS_PORTAL_DB"
    #   AIMS_CLIENT_DATABASE_URL=
    #   replace_with_old_variable_value DATABASE_URL AIMS_CLIENT_DATABASE_URL
    #

    old_variable=$1
    new_variable=$2

    # Expand old_variable value into old_variable_value
    eval "old_variable_value=\$$old_variable"

    if [ -n "$old_variable_value" ]; then
        # Old variable value is not empty, show depreciated message
        echo "Warning: \$${old_variable} is depreciated use \$${new_variable}"

        # Check if new variable is empty
        eval "new_variable_value=\$$new_variable"
        if [ -z "$new_variable_value" ]; then
            echo "New variable \$${new_variable} is empty..."
            echo "Replacing \$${old_variable} with \$${new_variable}"
            eval "$new_variable=$old_variable_value"
        fi
    fi

}

replace_empty_variable_with_default_value() {
    #
    # Description:
    #   Replace empty variable with default value
    #
    # Usage:
    #   replace_empty_variable_with_default_value variable_name default_value
    #
    # Example:
    #   AIMS_CLIENT_DATABASE_URL=""
    #   replace_empty_variable_with_default_value AIMS_CLIENT_DATABASE_URL jdbc:postgresql://127.0.0.1:6010/AIMS_PORTAL_DB
    #   echo $AIMS_CLIENT_DATABASE_URL

    variable_name=$1
    default_value=$2

    # Expand variable value into variable_value
    eval "variable_value=\$$variable_name"

    if [ -z "$variable_value" ]; then
        echo "Variable \$$variable_name is empty using default value $default_value"
        export ${variable_name}=${default_value}
    fi
}

replace_property() {
    property_name=$1
    property_value=$2

    sed -i -e "s|${property_name}=.*|${property_name}=${property_value}|" "/app/application.properties"
}

java_memory_settings() {
    echo "
-------------------------------------
Memory Settings
-------------------------------------"
    [ -n "${MEMORY_LIMIT+X}" ] && echo "Memory Limit: ${MEMORY_LIMIT} bytes" || echo "Discovering Memory Limit"
    [ -z "${MEMORY_LIMIT+X}" ] && [ -f /sys/fs/cgroup/memory/memory.limit_in_bytes ] &&
        MEMORY_LIMIT="$(cat /sys/fs/cgroup/memory/memory.limit_in_bytes)" &&
        echo "cgroup v1 limit: ${MEMORY_LIMIT} bytes"
    [ -z "${MEMORY_LIMIT+X}" ] && [ -f /sys/fs/cgroup/memory.max ] &&
        MEMORY_LIMIT="$(cat /sys/fs/cgroup/memory.max)" &&
        echo "cgroup v2 limit: ${MEMORY_LIMIT} bytes"

    if [ "${MEMORY_LIMIT}" = "max" ]; then
        echo "no cgroup limit -> 2GiB"
        MEMORY_LIMIT="$((2 * 1024 * 1024 * 1024))"
    fi

    [ -z "${JAVA_MAX_RAM_PERCENTAGE+X}" ] && JAVA_MAX_RAM_PERCENTAGE=75
    local cgroup_xmx_val="$((MEMORY_LIMIT * JAVA_MAX_RAM_PERCENTAGE / 100 / 1024 / 1024))m"

    # Specifically configured JAVA_XMX & JAVA_XMS takes precedence
    if [ -n "${JAVA_XMX}" ]; then
        echo "JAVA_XMX Env-Variable found, remove if you want container limit based RAM Percentage"
        JAVA_RAM_OPTS="-Xmx${JAVA_XMX}"
        echo "Java Max Heap Setting: ${JAVA_XMX}"
        [ -n "${JAVA_XMS}" ] &&
            JAVA_RAM_OPTS="${JAVA_RAM_OPTS} -Xms${JAVA_XMS}" &&
            echo "Java Min Heap Setting: ${JAVA_XMS}"
        echo "Container Limit based would have been equivalent to Xmx${cgroup_xmx_val}"
    else
        JAVA_RAM_OPTS="-XX:MaxRAM=${MEMORY_LIMIT} -XX:MaxRAMPercentage=${JAVA_MAX_RAM_PERCENTAGE}.0"
        echo "Java Max RAM Percentage: ${JAVA_MAX_RAM_PERCENTAGE}"
        echo "Equivalent to Xmx${cgroup_xmx_val}"
    fi
    echo "-------------------------------------
"
}
#
# Variables
#

## Use old variable value if new variable is not provided
## Usage: replace_with_old_variable_value <old_variable> <new_variable>
#replace_with_old_variable_value SERVER_PORT AIMS_CLIENT_SERVER_PORT

## Set empty variables with default value
## Usage: replace_empty_variable_with_default_value <variable_name> <variable_value>

replace_empty_variable_with_default_value SERVER_PORT "8080"
replace_empty_variable_with_default_value LOGGING_LEVEL_COM_SOLUM "${LOGLEVEL:-INFO}"

export SERVER_PORT="${SERVER_PORT}"
export LOGGING_LEVEL_COM_SOLUM="${LOGGING_LEVEL_COM_SOLUM}"
export MANAGEMENT_SERVER_PORT=9090
export MANAGEMENT_ENDPOINTS_WEB_BASEPATH=/actuator

# replace_property "spring.datasource.url" "jdbc:postgresql://$DATABASE_PORTAL_IP:$DATABASE_PORTAL_PORT/$DATABASE_PORTAL_NAME"
# replace_property "spring.datasource.username" "$DATABASE_PORTAL_USERNAME"
# replace_property "spring.datasource.password" "$DATABASE_PORTAL_PASSWORD"
# replace_property "spring.jpa.hibernate.ddl-auto" "update"

java_memory_settings

echo "
--------------------------------------------------------------------------
AIMS Shelf System
--------------------------------------------------------------------------
"

#
# Exec Java Application
#

exec /opt/java/openjdk/bin/java ${JAVA_RAM_OPTS} ${JAVA_OPTS} \
    -Djava.security.egd=file:/dev/./urandom \
    -jar /app/aims-saas-rossmann-shelf-system.jar
