import { StyleSheet, Text, useColorScheme, View } from 'react-native';
import React, { Fragment } from 'react';
import { Colors } from 'react-native/Libraries/NewAppScreen';

const links = [
  {
    id: 1,
    title: 'Permission',
    description: 'Please request all required permissions at first',
  },
  {
    id: 2,
    title: 'Utilities',
    description:
      'Please prompt users that enable location and bluetooth all the time',
  },
  {
    id: 3,
    title: 'SDK Key',
    description: 'Dont expose sdk key in code, get it through your own api',
  },
];

const LinkList = () => {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.container}>
      {links.map(({ id, title, description }) => (
        <Fragment key={id}>
          <View
            style={[
              styles.separator,
              {
                backgroundColor: isDarkMode ? Colors.dark : Colors.light,
              },
            ]}
          />
          <View style={styles.linkContainer}>
            <Text style={styles.link}>{title}</Text>
            <Text
              style={[
                styles.description,
                {
                  color: isDarkMode ? Colors.lighter : Colors.dark,
                },
              ]}
            >
              {description}
            </Text>
          </View>
        </Fragment>
      ))}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  linkContainer: {
    flexWrap: 'wrap',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 8,
  },
  link: {
    flex: 2,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.primary,
  },
  description: {
    flex: 3,
    paddingVertical: 16,
    fontWeight: '400',
    fontSize: 18,
  },
  separator: {
    height: StyleSheet.hairlineWidth,
  },
});

export default LinkList;
